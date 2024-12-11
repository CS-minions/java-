百度翻译API实现文档

1. 代码结构
BaiduTranslator.java 实现了百度翻译API的调用，主要包含：
- 配置信息（API URL、APP ID、密钥）
- 翻译方法（translate）
- 签名生成方法（generateSign）

2. 核心代码解释

2.1 配置信息
```java
private static final String BAIDU_API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
private static final String APP_ID = "20241206002221399";
private static final String SECRET_KEY = "CV4wPA8k2BmgLB9I9ORq";
```
这些常量用于存储API调用所需的基本配置。

2.2 翻译方法实现
```java
public static String translate(String text, String from, String to) {
    try {
        // 生成salt和sign
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

        // 发送GET请求
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
        
        // 解析响应
        String responseStr = response.toString();
        int dstIndex = responseStr.indexOf("\"dst\":\"");
        if (dstIndex != -1) {
            dstIndex += 7;
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
```

方法说明：
1. 参数说明
   - text: 待翻译的文本
   - from: 源语言（zh/en）
   - to: 目标语言（en/zh）

2. 实现步骤
   - 生成时间戳作为salt
   - 生成签名sign
   - 构建带参数的URL
   - 发送GET请求
   - 读取并解析响应

2.3 签名生成方法
```java
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
```

签名生成说明：
1. 拼接字符串：APP_ID + 原文 + salt + 密钥
2. 使用MD5算法生成签名
3. 将字节转换为16进制字符串

3. 特点说明

3.1 请求方式
- 使用GET方法发送请求
- URL参数包含所有必要信息
- 使用UTF-8编码处理中文

3.2 响应处理
- 简单的字符串解析方式
- 提取"dst"字段的值
- 异常情况返回错误信息

3.3 错误处理
- 捕获并处理网络异常
- 处理响应解析异常
- 提供友好的错误提示

4. 使用示例

中译英：
```java
String result = BaiduTranslator.translate(
    "你好，世界",
    "zh",  // 源语言：中文
    "en"   // 目标语言：英文
);
```

英译中：
```java
String result = BaiduTranslator.translate(
    "Hello, World",
    "en",  // 源语言：英文
    "zh"   // 目标语言：中文
);
```

5. 注意事项
1. API密钥安全
   - APP_ID和SECRET_KEY需要妥善保管
   - 建议使用配置文件存储

2. 编码处理
   - 使用UTF-8处理中文
   - URL编码处理特殊字符

3. 异常处理
   - 网络连接异常
   - 响应解析异常
   - 签名生成异常