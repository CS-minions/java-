import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIModelTranslator {
    // SiliconFlow API配置
    private static final String SILICON_API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String SILICON_API_KEY = "sk-luryegdguvvfrvblmladqxbryrxgpnlrbntuistuxdgngtme";

    // 百度文心一言API配置
    private static final String ERNIE_API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";
    private static final String ERNIE_API_KEY = "YOUR_ERNIE_API_KEY";
    private static final String ERNIE_SECRET_KEY = "YOUR_ERNIE_SECRET_KEY";

    // 阿里通义千问API配置
    private static final String QIANWEN_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    private static final String QIANWEN_API_KEY = "YOUR_QIANWEN_API_KEY";

    public static String translateWithSilicon(String text, String from, String to) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return "翻译失败：文本不能为空";
            }

            // 构建提示词
            String systemPrompt = String.format(
                "你是一个专业的翻译助手。请将用户输入的%s文本翻译成%s，只返回翻译结果，不要有任何解释。",
                getLanguageName(from), getLanguageName(to)
            );

            // 构建请求体
            String requestBody = String.format(
                "{" +
                "\"model\":\"Qwen/Qwen2.5-Coder-32B-Instruct\"," +
                "\"messages\":[" +
                    "{\"role\":\"system\",\"content\":\"%s\"}," +
                    "{\"role\":\"user\",\"content\":\"%s\"}" +
                "]," +
                "\"temperature\":0.7," +
                "\"top_p\":0.95," +
                "\"presence_penalty\":0," +
                "\"frequency_penalty\":0," +
                "\"stream\":false" +
                "}",
                systemPrompt.replace("\"", "\\\""),
                text.replace("\"", "\\\"")
            );

            System.out.println("请求体: " + requestBody); // 调试输出

            // 发送HTTP请求
            URL url = new URL(SILICON_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + SILICON_API_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);  // 增加超时时间到30秒
            conn.setReadTimeout(30000);     // 增加读取超时时间到30秒

            // 写入请求体
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(requestBody);
                writer.flush();
            }

            // 检查响应码
            int responseCode = conn.getResponseCode();
            System.out.println("响应码: " + responseCode); // 调试输出
            
            if (responseCode != 200) {
                // 读取错误信息
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                        responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream(), 
                        StandardCharsets.UTF_8
                    )
                )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                }
                System.out.println("错误响应: " + errorResponse.toString());
                return "翻译失败：服务器返回错误 " + responseCode + "\n" + errorResponse.toString();
            }

            // 读取成功响应
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
            if (contentStart >= 11 && contentEnd != -1 && contentEnd > contentStart) {
                String result = responseStr.substring(contentStart, contentEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .trim();
                
                // 如果结果是空的，返回错误信息
                if (result.isEmpty()) {
                    return "翻译失败：返回结果为空";
                }
                
                return result;
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

    public static String translateWithErnie(String text, boolean isChToEng) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return "翻译失败：文本不能为空";
            }

            String prompt = isChToEng ? 
                "请将以下中文翻译成英文：" + text :
                "请将以下英文翻译成中文：" + text;

            String requestBody = String.format("{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}", 
                prompt.replace("\"", "\\\""));
            
            URL url = new URL(ERNIE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + ERNIE_API_KEY);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(requestBody);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            String responseStr = response.toString();
            System.out.println("API响应: " + responseStr); // 调试输出

            // 提取翻译结果
            int resultStart = responseStr.indexOf("\"result\":\"") + 9;
            int resultEnd = responseStr.indexOf("\"", resultStart);
            if (resultStart >= 9 && resultEnd != -1) {
                return responseStr.substring(resultStart, resultEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            }

            return "翻译失败：无法解析响应";

        } catch (Exception e) {
            e.printStackTrace();
            return "文心一言翻译出错: " + e.getMessage();
        }
    }

    public static String translateWithQianwen(String text, boolean isChToEng) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return "翻译失败：文本不能为空";
            }

            String prompt = isChToEng ? 
                "请将以下中文翻译成英文：" + text :
                "请将以下英文翻译成中文：" + text;

            String requestBody = String.format(
                "{\"model\":\"qwen-turbo\",\"input\":{\"prompt\":\"%s\"}}", 
                prompt.replace("\"", "\\\""));

            URL url = new URL(QIANWEN_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + QIANWEN_API_KEY);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(requestBody);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            String responseStr = response.toString();
            System.out.println("API响应: " + responseStr); // 调试输出

            // 提取翻译结果
            int outputStart = responseStr.indexOf("\"output\":\"") + 9;
            int outputEnd = responseStr.indexOf("\"", outputStart);
            if (outputStart >= 9 && outputEnd != -1) {
                return responseStr.substring(outputStart, outputEnd)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            }

            return "翻译失败：无法解析响应";

        } catch (Exception e) {
            e.printStackTrace();
            return "通义千问翻译出错: " + e.getMessage();
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