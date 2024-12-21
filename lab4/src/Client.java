import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    
    private String clientName;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;

    public Client(String name) {
        this.clientName = name;
        
        // 创建GUI
        frame = new JFrame(name + " - 聊天窗口");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        
        inputField = new JTextField(50);
        inputField.addActionListener(e -> {
            out.println(inputField.getText());
            inputField.setText("");
        });
        frame.add(inputField, BorderLayout.SOUTH);
        
        frame.pack();
        
        // 设置窗口
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (name.equals("ClientA")) {
            frame.setLocation(0, 0);
        } else if (name.equals("ClientB")) {
            frame.setLocation((int)screenSize.getWidth() - frame.getWidth(), 0);
        } else {
            frame.setLocation((int)screenSize.getWidth()/2 - frame.getWidth()/2, 
                            (int)screenSize.getHeight() - frame.getHeight());
        }
    }

    private void connectToServer() throws IOException {
        // 连接到服务器
        socket = new Socket(SERVER_IP, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // 发送客户端名称
        out.println(clientName);

        // 启动消息接收线程
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message;
                    SwingUtilities.invokeLater(() -> {
                        messageArea.append(finalMessage + "\n");
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    messageArea.append("与服务器断开连接\n");
                });
            }
        }).start();
    }

    public void start() {
        frame.setVisible(true);
        try {
            connectToServer();
        } catch (IOException e) {
            messageArea.append("无法连接到服务器\n");
        }
    }

    public static void main(String[] args) {
        // 启动三个客户端
        SwingUtilities.invokeLater(() -> {
            new Client("ClientA").start();
            new Client("ClientB").start();
            new Client("ClientC").start();
        });
    }
} 