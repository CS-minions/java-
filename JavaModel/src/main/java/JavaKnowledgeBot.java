import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaKnowledgeBot extends JFrame {

    private JComboBox<String> vendorComboBox;
    private JTextArea chatTextArea;
    private JTextField inputTextField;
    private JButton sendButton;
    private ExecutorService threadPool;

    // 定义可用的厂商选项
    private static final String[] VENDORS = {"阿里-通义千问", "百度-文心一言"};

    public JavaKnowledgeBot() {
        setTitle("知识问答机器人");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化线程池用于并行处理请求
        threadPool = Executors.newCachedThreadPool();

        // 创建布局
        setLayout(new BorderLayout());

        // 左边面板 - 模型选择和配置
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        vendorComboBox = new JComboBox<>(VENDORS);
        vendorComboBox.addActionListener(this::onVendorChange); // 添加厂商切换监听器
        leftPanel.add(vendorComboBox);
        add(leftPanel, BorderLayout.WEST);

        // 右边面板 - 聊天显示和输入
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // 输入和发送按钮
        JPanel inputPanel = new JPanel();
        inputTextField = new JTextField(40);
        sendButton = new JButton("发送");
        sendButton.addActionListener(this::onSendMessage);
        inputPanel.add(inputTextField);
        inputPanel.add(sendButton);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // 初始加载默认厂商的聊天记录
        loadChatRecord();
    }

    private void onSendMessage(ActionEvent e) {
        String userInput = inputTextField.getText().trim();
        if (!userInput.isEmpty()) {
            // 显示用户消息
            chatTextArea.append("User: " + userInput + "\n");

            // 保存用户消息到本地
            saveChatRecord("User: " + userInput);

            // 清空输入框
            inputTextField.setText("");

            // 在后台线程中处理用户消息并与大模型交互
            threadPool.submit(() -> {
                try {
                    // 根据当前选择的厂商创建API实例
                    ModelAPI api = createAPIInstance((String) vendorComboBox.getSelectedItem());

                    // 调用API并获取回复
                    String modelResponse = api.call(userInput);
                    SwingUtilities.invokeLater(() -> {
                        // 更新UI，显示大模型回复
                        chatTextArea.append("Bot: " + modelResponse + "\n");
                        // 保存大模型回复到本地
                        saveChatRecord("Bot: " + modelResponse);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        chatTextArea.append("Error: " + ex.getMessage() + "\n");
                        // 保存错误信息到本地
                        saveChatRecord("Error: " + ex.getMessage());
                    });
                }
            });
        }
    }

    private synchronized void saveChatRecord(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLogFilePath(), true))) {
            writer.write(message);
            writer.newLine(); // 确保每条记录在新的一行开始
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChatRecord() {
        String logFilePath = getLogFilePath();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chatTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            // 文件不存在或读取失败，可能是首次使用或没有之前的记录
            // 可以选择忽略或者通知用户
        }
    }

    private void onVendorChange(ActionEvent e) {
        // 当厂商选择改变时，重新加载对应厂商的日志记录
        chatTextArea.setText(""); // 清空当前聊天记录
        loadChatRecord(); // 加载新选厂商的聊天记录
    }

    private String getLogFilePath() {
        // 根据选择的厂商返回不同的日志文件路径
        String selectedVendor = (String) vendorComboBox.getSelectedItem();
        return "chat_log_" + selectedVendor.replace(" ", "_") + ".txt";
    }

    private ModelAPI createAPIInstance(String vendorModelPair) {
       return ModelAPIFactory.createAPI(vendorModelPair);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JavaKnowledgeBot bot = new JavaKnowledgeBot();
            bot.setVisible(true);
        });
    }
}