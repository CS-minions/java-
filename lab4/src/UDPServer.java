import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {
    private static final int PORT = 12345;
    private static Map<String, ClientInfo> clients = new HashMap<>();
    private static DatagramSocket socket;

    private static class ClientInfo {
        InetAddress address;
        int port;

        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }

    public static void main(String[] args) {
        System.out.println("UDP服务器启动，等待客户端连接...");
        
        try {
            socket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[1024];

            while (true) {
                // 接收数据包
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // 处理接收到的数据
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // 处理消息
                processMessage(message, clientAddress, clientPort);
            }
        } catch (IOException e) {
            System.out.println("服务器异常: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void processMessage(String message, InetAddress address, int port) {
        // 解析消息格式：类型|客户端名称|内容
        String[] parts = message.split("\\|", 3);
        if (parts.length < 2) return;

        String type = parts[0];
        String clientName = parts[1];
        String content = parts.length > 2 ? parts[2] : "";

        switch (type) {
            case "JOIN":
                // 新客户端加入
                clients.put(clientName, new ClientInfo(address, port));
                broadcast("SYSTEM|系统|" + clientName + " 加入了聊天室");
                break;

            case "MESSAGE":
                // 转发消息
                if (content.length() > 0) {
                    broadcast("MESSAGE|" + clientName + "|" + content);
                }
                break;

            case "LEAVE":
                // 客户端离开
                clients.remove(clientName);
                broadcast("SYSTEM|系统|" + clientName + " 离开了聊天室");
                break;
        }
    }

    private static void broadcast(String message) {
        byte[] sendData = message.getBytes();
        for (Map.Entry<String, ClientInfo> entry : clients.entrySet()) {
            try {
                ClientInfo client = entry.getValue();
                DatagramPacket sendPacket = new DatagramPacket(
                    sendData, 
                    sendData.length, 
                    client.address, 
                    client.port
                );
                socket.send(sendPacket);
            } catch (IOException e) {
                System.out.println("发送消息给 " + entry.getKey() + " 失败: " + e.getMessage());
            }
        }
    }
} 