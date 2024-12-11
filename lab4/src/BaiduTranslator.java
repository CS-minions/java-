import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BaiduTranslator {
    private static final String BAIDU_API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String APP_ID = "20241206002221399";
    private static final String SECRET_KEY = "CV4wPA8k2BmgLB9I9ORq";

    public static String translate(String text, String from, String to) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return "翻译失败：文本不能为空";
            }

            String salt = String.valueOf(System.currentTimeMillis());
            String sign = generateSign(text, salt);
            
            // 构建请求URL
            String urlStr = String.format("%s?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s",
                BAIDU_API_URL,
                URLEncoder.encode(text, "UTF-8"),
                from,
                to,
                APP_ID,
                salt,
                sign);

            // 发送HTTP请求
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            // 读取响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            // 解析响应
            String responseStr = response.toString();
            System.out.println("API响应: " + responseStr); // 调试输出
            
            // 检查是否有错误
            if (responseStr.contains("\"error_code\"")) {
                int errorCodeStart = responseStr.indexOf("\"error_code\":\"") + 13;
                int errorCodeEnd = responseStr.indexOf("\"", errorCodeStart);
                int errorMsgStart = responseStr.indexOf("\"error_msg\":\"") + 13;
                int errorMsgEnd = responseStr.indexOf("\"", errorMsgStart);
                
                String errorCode = responseStr.substring(errorCodeStart, errorCodeEnd);
                String errorMsg = responseStr.substring(errorMsgStart, errorMsgEnd);
                return String.format("翻译失败：错误码 %s, 错误信息 %s", errorCode, errorMsg);
            }
            
            // 解析翻译结果
            int transResultStart = responseStr.indexOf("\"trans_result\":[{");
            if (transResultStart >= 0) {
                int dstStart = responseStr.indexOf("\"dst\":\"", transResultStart) + 7;
                if (dstStart >= 7) {
                    int dstEnd = responseStr.indexOf("\"},", dstStart);
                    if (dstEnd == -1) {
                        dstEnd = responseStr.indexOf("\"}]}", dstStart);
                    }
                    if (dstEnd != -1) {
                        String translatedText = responseStr.substring(dstStart, dstEnd)
                            .replace("\\\"", "\"")  // 处理转义的引号
                            .replace("\\\\", "\\"); // 处理转义的反斜杠
                        return translatedText;
                    }
                }
            }
            
            return "翻译失败：未找到翻译结果";
            
        } catch (java.net.SocketTimeoutException e) {
            return "翻译失败：连接超时，请检查网络";
        } catch (java.net.UnknownHostException e) {
            return "翻译失败：无法连接到翻译服务器";
        } catch (Exception e) {
            e.printStackTrace();
            return "翻译失败：" + e.getMessage();
        }
    }

    private static String generateSign(String text, String salt) {
        try {
            String str = APP_ID + text + salt + SECRET_KEY;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sign = new StringBuilder();
            for (byte b : bytes) {
                sign.append(String.format("%02x", b & 0xff));
            }
            return sign.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
} 