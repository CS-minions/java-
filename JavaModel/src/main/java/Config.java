public class Config {
    // 阿里云配置
    public static final String ALIYUN_API_KEY = "sk-2c51c448693f437ab806cea4b110d2a9";
    public static final String ALIYUN_MODEL = "qwen-plus";

    // 百度配置
    public static final String BAIDU_API_KEY = "";
    public static final String BAIDU_SECRET_KEY = "";
    public static final String BAIDU_API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=";

    // 禁止实例化
    private Config() {}
}