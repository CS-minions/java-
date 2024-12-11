import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class BaiduAPI implements ModelAPI {

    private final String apiKey;
    private final String secretKey;
    private final String apiUrl;
    private final OkHttpClient httpClient = new OkHttpClient();

    public BaiduAPI() {
        this.apiKey = Config.BAIDU_API_KEY;
        this.secretKey = Config.BAIDU_SECRET_KEY; // 添加 Secret Key 的引用
        this.apiUrl = Config.BAIDU_API_URL;
    }

    @Override
    public String call(String userInput) throws Exception {
//        String accessToken = getAccessToken();
        String url = apiUrl + "?access_token=" + "24.17c0532bd56255f6f169e3ea62c31ec1.2592000.1736167420.282335-116574716";


        MediaType mediaType = MediaType.parse("application/json");

        // 创建 JSON 对象并添加必要的字段
        // 构建请求体
        JSONObject requestBodyJson = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject messageObject = new JSONObject();
        messageObject.put("role", "user");
        messageObject.put("content", userInput);
        messagesArray.put(messageObject);
        requestBodyJson.put("messages", messagesArray);


        // 将 JSON 对象转换为字符串并设置为请求体
        RequestBody body = RequestBody.create(mediaType, requestBodyJson.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            System.out.println(response);

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            // 根据实际返回结果解析相应数据
            return jsonResponse.get("result").toString(); // 美化输出JSON字符串，缩进为4个空格
        }
    }

    /**
     * 获取访问令牌（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + apiKey
                + "&client_secret=" + secretKey);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("access_token");
        }
    }
}