import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.util.Arrays;

public class AliyunAPI implements ModelAPI {

    private final String apiKey;
    private final String model;

    public AliyunAPI() {
        this.apiKey = Config.ALIYUN_API_KEY;
        this.model = Config.ALIYUN_MODEL;
    }

    @Override
    public String call(String userInput) throws ApiException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(userInput)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        GenerationResult result = null;
        try {
            result = gen.call(param);
        } catch (NoApiKeyException e) {
            e.printStackTrace();
        } catch (InputRequiredException e) {
            e.printStackTrace();
        }
        return extractContentFromResult(result);
    }

    private String extractContentFromResult(GenerationResult result) {
        if (result.getOutput() != null && !result.getOutput().getChoices().isEmpty()) {
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        }
        return "Sorry, I couldn't generate a response.";
    }
}