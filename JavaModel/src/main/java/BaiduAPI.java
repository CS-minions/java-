import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaiduAPI implements ModelAPI {

    private final String apiKey;
    private final String secretKey;
    private final String apiUrl;
    private final OkHttpClient httpClient;

    public BaiduAPI() {
        this.apiKey = Config.BAIDU_API_KEY;
        this.secretKey = Config.BAIDU_SECRET_KEY;
        this.apiUrl = Config.BAIDU_API_URL;
        
        // 配置 OkHttpClient
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)      // 连接超时
            .writeTimeout(30, TimeUnit.SECONDS)        // 写入超时
            .readTimeout(60, TimeUnit.SECONDS)         // 读取超时
            .build();
    }

    @Override
    public String call(String userInput) throws Exception {
        try {
            String accessToken = getAccessToken();
            String url = apiUrl + accessToken;

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userInput);
            messages.put(userMessage);
            requestBody.put("messages", messages);

            // 发送请求
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(
                        MediaType.parse("application/json"), 
                        requestBody.toString()
                    ))
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("API 调用失败: " + response.code() + " " + response.message());
                }

                JSONObject jsonResponse = new JSONObject(response.body().string());
                
                if (jsonResponse.has("error_code")) {
                    throw new IOException("API 错误: " + 
                        jsonResponse.getInt("error_code") + " - " + 
                        jsonResponse.getString("error_msg"));
                }

                // 处理新的返回格式
                Object result = jsonResponse.get("result");
                if (result instanceof JSONObject) {
                    return ((JSONObject) result).getString("content");
                } else if (result instanceof String) {
                    return (String) result;
                } else {
                    throw new IOException("未知的返回格式");
                }
            }
        } catch (Exception e) {
            System.err.println("API 调用出错: " + e.getMessage());
            return "抱歉，调用服务时出现错误：" + e.getMessage();
        }
    }

    private String getAccessToken() throws IOException {
        String authUrl = "https://aip.baidubce.com/oauth/2.0/token";
        
        Request request = new Request.Builder()
                .url(authUrl + 
                     "?grant_type=client_credentials" +
                     "&client_id=" + apiKey +
                     "&client_secret=" + secretKey)
                .post(RequestBody.create(
                    MediaType.parse("application/json"), 
                    ""
                ))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("认证失败: " + response.code() + " " + response.message());
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("access_token");
        }
    }
}