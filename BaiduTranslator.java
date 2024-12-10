import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class BaiduTranslator {
    private static final String BAIDU_API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String APP_ID = "YOUR_APP_ID"; // 替换为你的百度翻译APP ID
    private static final String SECRET_KEY = "YOUR_SECRET_KEY"; // 替换为你的百度翻译密钥

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
            
            // TODO: 解析JSON响应并返回翻译结果
            return response.toString();
            
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