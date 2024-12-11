# AIModelTranslator 代码说明文档

## 1. 概述

`AIModelTranslator` 是一个集成了多个AI翻译API的Java类，提供了多种AI模型的翻译功能。目前支持以下三种翻译服务：
- SiliconFlow的通义千问翻译
- 百度文心一言翻译
- 阿里通义千问翻译

## 2. 类结构

### 2.1 常量定义

```java
// SiliconFlow API配置
private static final String SILICON_API_URL = "https://api.siliconflow.cn/v1/chat/completions";
private static final String SILICON_API_KEY = "your_api_key";

// 百度文心一言API配置
private static final String ERNIE_API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";
private static final String ERNIE_API_KEY = "your_api_key";

// 阿里通义千问API配置
private static final String QIANWEN_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
private static final String QIANWEN_API_KEY = "your_api_key";
```

### 2.2 主要方法

#### 2.2.1 SiliconFlow翻译方法

```java
public static String translateWithSilicon(String text, String from, String to) {
    try {
        // 1. 输入验证
        if (text == null || text.trim().isEmpty()) {
            return "翻译失败：文本不能为空";
        }

        // 2. 构建system提示词
        String systemPrompt = String.format(
            "你是一个专业的翻译助手。请将用户输入的%s文本翻译成%s，只返回翻译结果，不要有任何解释。",
            getLanguageName(from), getLanguageName(to)
        );

        // 3. 构建API请求体
        String requestBody = String.format(
            "{" +
            "\"model\":\"glm-4\"," +
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

        // 4. 发送HTTP请求
        URL url = new URL(SILICON_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + SILICON_API_KEY);
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        // 5. 写入请求数据
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
            writer.write(requestBody);
            writer.flush();
        }

        // 6. 处理响应
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
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
            return "翻译失败：服务器返回错误 " + responseCode + "\n" + errorResponse.toString();
        }

        // 7. 读取响应数据
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        // 8. 解析响应JSON
        String responseStr = response.toString();
        int contentStart = responseStr.indexOf("\"content\":\"") + 11;
        int contentEnd = responseStr.lastIndexOf("\"");
        if (contentStart >= 11 && contentEnd != -1 && contentEnd > contentStart) {
            return responseStr.substring(contentStart, contentEnd)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .trim();
        }

        return "翻译失败：无法解析响应";

    } catch (Exception e) {
        e.printStackTrace();
        return "翻译失败：" + e.getMessage();
    }
}
```

- **功能**: 使用SiliconFlow的通义千问模型进行翻译
- **参数**:
  - `text`: 要翻译的文本
  - `from`: 源语言代码（如"zh"）
  - `to`: 目标语言代码（如"en"）
- **实现细节**:
  1. 输入验证
  2. 构建system提示词
  3. 构建API请求体
  4. 发送HTTP请求
  5. 处理响应结果

#### 2.2.2 文心一言翻译方法

```java
public static String translateWithErnie(String text, boolean isChToEng) {
    try {
        // 1. 输入验证
        if (text == null || text.trim().isEmpty()) {
            return "翻译失败：文本不能为空";
        }

        // 2. 构建提示词
        String prompt = isChToEng ? 
            "请将以下中文翻译成英文：" + text :
            "请将以下英文翻译成中文：" + text;

        // 3. 构建请求体
        String requestBody = String.format(
            "{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}", 
            prompt.replace("\"", "\\\"")
        );

        // 4. 发送请求和处理响应
        URL url = new URL(ERNIE_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + ERNIE_API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        // 5. 写入请求数据
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
            writer.write(requestBody);
        }

        // 6. 读取响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // 7. 解析响应
        String responseStr = response.toString();
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
```

- **功能**: 使用百度文心一言API进行翻译
- **参数**:
  - `text`: 要翻译的文本
  - `isChToEng`: 是否为中译英
- **实现细节**:
  1. 输入验证
  2. 构建翻译提示词
  3. 发送API请求
  4. 解析响应结果

#### 2.2.3 通义千问翻译方法

```java
public static String translateWithQianwen(String text, boolean isChToEng) {
    try {
        // 1. 输入验证
        if (text == null || text.trim().isEmpty()) {
            return "翻译失败：文本不能为空";
        }

        // 2. 构建提示词
        String prompt = isChToEng ? 
            "请将以下中文翻译成英文：" + text :
            "请将以下英文翻译成中文：" + text;

        // 3. 构建请求体
        String requestBody = String.format(
            "{\"model\":\"qwen-turbo\",\"input\":{\"prompt\":\"%s\"}}", 
            prompt.replace("\"", "\\\"")
        );

        // 4. 发送请求
        URL url = new URL(QIANWEN_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + QIANWEN_API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        // 5. 写入请求数据
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
            writer.write(requestBody);
        }

        // 6. 读取响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // 7. 解析响应
        String responseStr = response.toString();
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
```

- **功能**: 使用阿里通义千问API进行翻译
- **参数**:
  - `text`: 要翻译的文本
  - `isChToEng`: 是否为中译英
- **实现细节**:
  1. 输入验证
  2. 构建翻译提示词
  3. 发送API请求
  4. 解析响应结果

## 3. 关键实现细节

### 3.1 HTTP请求处理

```java
URL url = new URL(API_URL);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("POST");
conn.setRequestProperty("Content-Type", "application/json");
conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
conn.setDoOutput(true);
conn.setConnectTimeout(30000);  // 30秒超时
conn.setReadTimeout(30000);
```

### 3.2 错误处理机制

```java
try {
    // API调用代码
} catch (java.net.SocketTimeoutException e) {
    return "翻译失败：连接超时，请检查网络";
} catch (java.net.UnknownHostException e) {
    return "翻译失败：无法连接到翻译服务器";
} catch (Exception e) {
    e.printStackTrace();
    return "翻译失败：" + e.getMessage();
}
```

### 3.3 响应解析

```java
// 提取翻译结果
int contentStart = responseStr.indexOf("\"content\":\"") + 11;
int contentEnd = responseStr.indexOf("\"", contentStart);
if (contentStart >= 11 && contentEnd != -1 && contentEnd > contentStart) {
    String result = responseStr.substring(contentStart, contentEnd)
        .replace("\\n", "\n")
        .replace("\\\"", "\"")
        .replace("\\\\", "\\")
        .trim();
    
    if (result.isEmpty()) {
        return "翻译失败：返回结果为空";
    }
    
    return result;
}
```

## 4. 使用示例

### 4.1 使用SiliconFlow翻译

```java
String result = AIModelTranslator.translateWithSilicon("你好世界", "zh", "en");
System.out.println(result);  // 输出: Hello world
```

### 4.2 使用文心一言翻译

```java
String result = AIModelTranslator.translateWithErnie("Hello world", false);
System.out.println(result);  // 输出: 你好世界
```

### 4.3 使用通义千问翻译

```java
String result = AIModelTranslator.translateWithQianwen("你好世界", true);
System.out.println(result);  // 输出: Hello world
```

## 5. 注意事项

1. **API密钥配置**
   - 使用前需要替换代码中的API密钥
   - 请妥善保管API密钥，不要泄露

2. **错误处理**
   - 所有方法都返回字符串类型
   - 翻译失败时返回错误信息
   - 建议在使用时增加错误检查

3. **网络要求**
   - 需要稳定的网络连接
   - 设置了30秒的超时时间
   - 建议增加重试机制

4. **性能考虑**
   - API调用有频率限制
   - 较长文本可能需要分段处理
   - 建议添加缓存机制

## 6. 可能的改进方向

1. 添加重试机制
2. 实现请求缓存
3. 支持更多的语言对
4. 添加并发请求限制
5. 实现异步翻译接口
6. 添加详细的日志记录
7. 实现翻译结果验证机制
8. 添加请求速率限制 