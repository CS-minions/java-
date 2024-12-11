import java.util.HashMap;
import java.util.Map;

public class TranslationService {
    private Map<String, String> localTranslations;
    private BaiduTranslator baiduTranslator;
    private GPTTranslator gptTranslator;
    private ErnieTranslator ernieTranslator;

    public TranslationService() {
        // 初始化本地翻译
        localTranslations = new HashMap<>();
        initializeLocalTranslations();
        
        // 初始化API翻译服务
        baiduTranslator = new BaiduTranslator();
        gptTranslator = new GPTTranslator();
        ernieTranslator = new ErnieTranslator();
    }

    private void initializeLocalTranslations() {
        localTranslations.put(
            "建校41年，深圳大学秉承\"自立、自律、自强\"的校训，紧随特区，锐意改革、快速发展，为特区发展和国家现代化建设做出了重要贡献。",
            "Sticking to the motto of \"self-reliance, self-discipline, self-improvement\", the University is dedicated to serving the Shenzhen Special Economic Zone (SEZ), demonstrating China's reform and opening up and pioneering change in higher education."
        );
        
        // 反向翻译对也加入Map
        localTranslations.put(
            "Sticking to the motto of \"self-reliance, self-discipline, self-improvement\", the University is dedicated to serving the Shenzhen Special Economic Zone (SEZ), demonstrating China's reform and opening up and pioneering change in higher education.",
            "建校41年，深圳大学秉承\"自立、自律、自强\"的校训，紧随特区，锐意改革、快速发展，为特区发展和国家现代化建设做出了重要贡献。"
        );
    }

    public String translateToEnglish(String chineseText) {
        // 1. 首先尝试本地翻译
        String localTranslation = localTranslations.get(chineseText);
        if (localTranslation != null) {
            return localTranslation;
        }

        // 2. 尝试百度API翻译
        try {
            return baiduTranslator.translateToEnglish(chineseText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. 尝试大模型API翻译
        try {
            // 可以选择使用GPT或文心一言
            return gptTranslator.translateToEnglish(chineseText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "翻译失败";
    }

    public String translateToChinese(String englishText) {
        // 实现类似的逻辑
        // ...
        return "翻译失败";
    }
} 