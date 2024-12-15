import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaKnowledgeBot extends JFrame {

    private JComboBox<String> vendorComboBox;
    private JTextPane chatTextPane;
    private JTextField inputTextField;
    private JButton sendButton;
    private ExecutorService threadPool;
    private StreamingResponse streamingResponse;
    private JButton pauseButton;
    private JSlider speedSlider;

    // 定义可用的厂商选项
    private static final String[] VENDORS = {"阿里-通义千问", "百度-文心一言"};

    public JavaKnowledgeBot() {
        setTitle("知识问答机器人");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeApplication();
            }
        });

        // 初始化线程池
        threadPool = Executors.newCachedThreadPool();

        // 创建布局
        setLayout(new BorderLayout(5, 5));

        // 创建所有面板和组件
        createPanels();
        
        // 初始化流式输出管理器
        streamingResponse = new StreamingResponse(chatTextPane);
        
        // 添加控制面板
        addControlPanel();

        // 初始加载默认厂商的聊天记录
        loadChatRecord();
    }

    private void createPanels() {
        // 左边面板 - 模型选择和配置
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("模型选择"));
        
        vendorComboBox = new JComboBox<>(VENDORS);
        vendorComboBox.setMaximumSize(new Dimension(200, 30));
        vendorComboBox.addActionListener(this::onVendorChange);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(vendorComboBox);
        leftPanel.add(Box.createVerticalStrut(10));
        add(leftPanel, BorderLayout.WEST);

        // 右边面板 - 聊天显示和输入
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // 聊天区域
        chatTextPane = new JTextPane();
        chatTextPane.setEditable(false);
        chatTextPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        chatTextPane.putClientProperty("JTextPane.lineWrap", Boolean.TRUE);
        chatTextPane.putClientProperty("caretWidth", 1);
        JScrollPane scrollPane = new JScrollPane(chatTextPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder("对话区域"));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // 输入和发送按钮
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        inputTextField = new JTextField();
        inputTextField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputTextField.addActionListener(this::onSendMessage);
        
        sendButton = new JButton("发送");
        sendButton.setPreferredSize(new Dimension(80, 30));
        sendButton.addActionListener(this::onSendMessage);
        
        inputPanel.add(inputTextField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void addControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        // 添加暂停/继续按钮
        pauseButton = new JButton("暂停");
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> {
            if (pauseButton.getText().equals("暂停")) {
                streamingResponse.pause();
                pauseButton.setText("继续");
            } else {
                streamingResponse.resume();
                pauseButton.setText("暂停");
            }
        });
        
        // 添加速度调节滑块
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        speedPanel.setBorder(BorderFactory.createTitledBorder("输出速度"));
        speedSlider = new JSlider(10, 200, 50);
        speedSlider.setPreferredSize(new Dimension(150, 30));
        speedSlider.addChangeListener(e -> 
            streamingResponse.setTypingSpeed(speedSlider.getValue())
        );
        speedPanel.add(speedSlider);
        
        // 添加跳过按钮
        JButton skipButton = new JButton("跳过");
        skipButton.setFocusPainted(false);
        skipButton.addActionListener(e -> streamingResponse.skipToEnd());
        
        controlPanel.add(speedPanel);
        controlPanel.add(pauseButton);
        controlPanel.add(skipButton);
        
        add(controlPanel, BorderLayout.NORTH);
    }

    private void onSendMessage(ActionEvent e) {
        String userInput = inputTextField.getText().trim();
        if (!userInput.isEmpty()) {
            // 禁用输入框和发送按钮
            setInputEnabled(false);
            
            // 显示用户消息
            displayMessage("User: " + userInput + "\n");
            saveChatRecord("User: " + userInput);
            inputTextField.setText("");

            // 异步处理AI响应
            threadPool.submit(() -> {
                try {
                    ModelAPI api = createAPIInstance((String) vendorComboBox.getSelectedItem());
                    String response = api.call(userInput);
                    
                    SwingUtilities.invokeLater(() -> {
                        // 使用流式输出显示AI响应
                        displayMessage("Bot: ");
                        streamingResponse.streamResponse(response + "\n");
                        saveChatRecord("Bot: " + response);
                        // 启用输入框和发送按钮
                        setInputEnabled(true);
                    });
                } catch (Exception ex) {
                    handleError(ex);
                    // 发生错误时也要启用输入框和发送按钮
                    SwingUtilities.invokeLater(() -> setInputEnabled(true));
                }
            });
        }
    }

    private void displayMessage(String message) {
        if (message.startsWith("User: ") || message.startsWith("Error: ")) {
            // 用户消息和错误消息直接显示
            chatTextPane.setText(chatTextPane.getText() + message);
        } else {
            // 只有新的AI响应使用流式输出
            streamingResponse.streamResponse(message);
        }
        chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());
    }

    private void handleError(Exception ex) {
        SwingUtilities.invokeLater(() -> {
            chatTextPane.setText(chatTextPane.getText() + "Error: " + ex.getMessage() + "\n");
            ex.printStackTrace();
            // 显示错误对话框
            JOptionPane.showMessageDialog(
                this,
                "发生错误: " + ex.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE
            );
        });
    }

    private synchronized void saveChatRecord(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLogFilePath(), true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            CodeHighlighter.insertText(chatTextPane, "Error: 保存聊天记录失败: " + e.getMessage() + "\n", false);
        }
    }

    private void loadChatRecord() {
        String logFilePath = getLogFilePath();
        File logFile = new File(logFilePath);
        
        // 如果文件不存在，创建新文件
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
                return; // 新文件创建后直接返回，因为内容为空
            } catch (IOException e) {
                handleError(new Exception("创建聊天记录文件失败: " + e.getMessage(), e));
                return;
            }
        }
        
        // 读取现有文件内容
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chatTextPane.setText(chatTextPane.getText() + line + "\n");
            }
        } catch (IOException e) {
            handleError(new Exception("加载聊天记录失败: " + e.getMessage(), e));
        }
    }

    private void onVendorChange(ActionEvent e) {
        // 当厂商选择改变时，重新加载对应厂商的日志记录
        chatTextPane.setText(""); // 清空当前聊天记录
        loadChatRecord(); // 加载新选厂商的聊天记录
    }

    private String getLogFilePath() {
        // 获取选中的厂商名称
        String selectedVendor = (String) vendorComboBox.getSelectedItem();
        // 使用 UTF-8 编码处理文件名
        try {
            String fileName = "chat_log_" + selectedVendor.replace(" ", "_") + ".txt";
            return new String(fileName.getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "chat_log_default.txt";
        }
    }

    private ModelAPI createAPIInstance(String vendorModelPair) {
       return ModelAPIFactory.createAPI(vendorModelPair);
    }

    // 添加资源清理方法
    private void closeApplication() {
        // 关闭线程池
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                // 等待所有任务完成
                if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
        }
        
        // 清理流式输出
        if (streamingResponse != null) {
            streamingResponse.cleanup();
        }
        
        // 保存最后的状态（如果需要）
        dispose();
        System.exit(0);
    }

    // 控制输入状态的辅助方法
    private void setInputEnabled(boolean enabled) {
        inputTextField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
        if (enabled) {
            inputTextField.requestFocusInWindow(); // 自动获取焦点
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JavaKnowledgeBot bot = new JavaKnowledgeBot();
            bot.setVisible(true);
        });
    }
}