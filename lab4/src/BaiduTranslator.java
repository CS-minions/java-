import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class BaiduTranslator {
    private static final String BAIDU_API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String APP_ID = "20241206002221399"; // 替换为你的百度翻译APP ID
    private static final String SECRET_KEY = "CV4wPA8k2BmgLB9I9ORq"; // 替换为你的百度翻译密钥

    public static String translate(String text, String from, String to) {
        try {
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
            
            // 读取响应
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            // 简单解析响应
            String responseStr = response.toString();
            // 查找 "dst":" 后面的内容
            int dstIndex = responseStr.indexOf("\"dst\":\"");
            if (dstIndex != -1) {
                dstIndex += 7; // "dst":" 的长度
                int endIndex = responseStr.indexOf("\"", dstIndex);
                if (endIndex != -1) {
                    return responseStr.substring(dstIndex, endIndex);
                }
            }
            return "翻译失败：未找到翻译结果";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "翻译出错: " + e.getMessage();
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