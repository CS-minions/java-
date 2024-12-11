import java.io.*;
import java.net.*;
import java.util.*;

public class ServerS {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("服务器启动，等待客户端连接...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // 设置输入输出流
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 获取客户端名称
                clientName = in.readLine();
                System.out.println(clientName + " 已连接");
                
                // 将客户端添加到Map中
                synchronized (clients) {
                    clients.put(clientName, out);
                }

                // 广播新客户端加入的消息
                broadcast(clientName + " 加入了聊天室");

                // 处理客户端消息
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(clientName + ": " + message);
                    broadcast(clientName + ": " + message);
                }
            } catch (IOException e) {
                System.out.println(clientName + " 连接异常: " + e.getMessage());
            } finally {
                // 清理资源
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clients) {
                    clients.remove(clientName);
                }
                broadcast(clientName + " 离开了聊天室");
            }
        }

        // 广播消息给所有客户端
        private void broadcast(String message) {
            synchronized (clients) {
                for (PrintWriter writer : clients.values()) {
                    writer.println(message);
                }
            }
        }
    }
} 