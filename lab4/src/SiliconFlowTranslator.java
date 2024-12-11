import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SiliconFlowTranslator {
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String API_KEY = "YOUR_API_KEY"; // 需要替换为实际的API密钥

    public static String translate(String text, String from, String to) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return "翻译失败：文本不能为空";
            }

            // 构建提示词
            String prompt = String.format("请将以下%s文本翻译成%s：\n%s", 
                getLanguageName(from), getLanguageName(to), text);

            // 构建请求体
            String requestBody = String.format(
                "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}],\"temperature\":0.7}",
                prompt.replace("\"", "\\\"")
            );

            // 发送HTTP请求
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // 写入请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 读取响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // 解析响应
            String responseStr = response.toString();
            System.out.println("API响应: " + responseStr); // 调试输出

            // 提取翻译结果
            int contentStart = responseStr.indexOf("\"content\":\"") + 11;
            int contentEnd = responseStr.indexOf("\"", contentStart);
            if (contentStart >= 11 && contentEnd != -1) {
                return responseStr.substring(contentStart, contentEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            }

            return "翻译失败：无法解析响应";

        } catch (java.net.SocketTimeoutException e) {
            return "翻译失败：连接超时，请检查网络";
        } catch (java.net.UnknownHostException e) {
            return "翻译失败：无法连接到翻译服务器";
        } catch (Exception e) {
            e.printStackTrace();
            return "翻译失败：" + e.getMessage();
        }
    }

    private static String getLanguageName(String code) {
        switch (code.toLowerCase()) {
            case "zh": return "中文";
            case "en": return "英文";
            case "ja": return "日文";
            case "ko": return "韩文";
            case "fr": return "法文";
            case "es": return "西班牙文";
            case "ru": return "俄文";
            case "de": return "德文";
            default: return code;
        }
    }
} 