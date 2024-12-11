import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UDPClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    
    private String clientName;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private volatile boolean running;

    public UDPClient(String name) {
        this.clientName = name;
        
        // 创建GUI
        frame = new JFrame(name + " - 聊天窗口");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendMessage("LEAVE", "");
                running = false;
                socket.close();
                frame.dispose();
            }
        });
        
        messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        
        inputField = new JTextField(50);
        inputField.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                sendMessage("MESSAGE", message);
                inputField.setText("");
            }
        });
        frame.add(inputField, BorderLayout.SOUTH);
        
        frame.pack();
        
        // 设置窗口位置
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

    private void start() {
        frame.setVisible(true);
        running = true;
        
        try {
            // 初始化UDP socket
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(SERVER_IP);
            
            // 发送加入消息
            sendMessage("JOIN", "");
            
            // 启动消息接收线程
            new Thread(this::receiveMessages).start();
            
        } catch (IOException e) {
            messageArea.append("连接失败: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage(String type, String content) {
        try {
            // 构造消息：类型|客户端名称|内容
            String message = type + "|" + clientName + "|" + content;
            byte[] sendData = message.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(
                sendData,
                sendData.length,
                serverAddress,
                SERVER_PORT
            );
            
            socket.send(sendPacket);
        } catch (IOException e) {
            messageArea.append("发送消息失败: " + e.getMessage() + "\n");
        }
    }

    private void receiveMessages() {
        byte[] receiveData = new byte[1024];
        
        while (running) {
            try {
                // 接收数据包
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                
                // 解析消息
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] parts = message.split("\\|", 3);
                if (parts.length < 3) continue;
                
                String type = parts[0];
                String sender = parts[1];
                String content = parts[2];
                
                // 在GUI中显示消息
                SwingUtilities.invokeLater(() -> {
                    if (type.equals("SYSTEM")) {
                        messageArea.append("[系统] " + content + "\n");
                    } else {
                        messageArea.append(sender + ": " + content + "\n");
                    }
                });
                
            } catch (IOException e) {
                if (running) {
                    SwingUtilities.invokeLater(() -> {
                        messageArea.append("接收消息失败: " + e.getMessage() + "\n");
                    });
                }
            }
        }
    }

    public static void main(String[] args) {
        // 启动三个客户端
        SwingUtilities.invokeLater(() -> {
            new UDPClient("ClientA").start();
            new UDPClient("ClientB").start();
            new UDPClient("ClientC").start();
        });
    }
} 