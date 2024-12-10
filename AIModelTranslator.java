import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIModelTranslator {
    // 百度文心一言API配置
    private static final String ERNIE_API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";
    private static final String ERNIE_API_KEY = "YOUR_ERNIE_API_KEY";
    private static final String ERNIE_SECRET_KEY = "YOUR_ERNIE_SECRET_KEY";

    // 阿里通义千问API配置
    private static final String QIANWEN_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    private static final String QIANWEN_API_KEY = "YOUR_QIANWEN_API_KEY";

    public static String translateWithErnie(String text, boolean isChToEng) {
        try {
            String prompt = isChToEng ? 
                "请将以下中文翻译成英文：" + text :
                "请将以下英文翻译成中文：" + text;

            String requestBody = String.format("{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}", prompt);
            
            URL url = new URL(ERNIE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + ERNIE_API_KEY);
            conn.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(requestBody);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // TODO: 解析JSON响应并返回翻译结果
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "文心一言翻译出错: " + e.getMessage();
        }
    }

    public static String translateWithQianwen(String text, boolean isChToEng) {
        try {
            String prompt = isChToEng ? 
                "请将以下中文翻译成英文：" + text :
                "请将以下英文翻译成中文：" + text;

            String requestBody = String.format("{\"model\":\"qwen-turbo\",\"input\":{\"prompt\":\"%s\"}}", prompt);

            URL url = new URL(QIANWEN_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + QIANWEN_API_KEY);
            conn.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(requestBody);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // TODO: 解析JSON响应并返回翻译结果
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "通义千问翻译出错: " + e.getMessage();
        }
    }
} 