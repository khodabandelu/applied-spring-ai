package dev.mehdi.springai.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiPromptService implements PromptService {

    private final ChatClient chatClient;

    public SpringAiPromptService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public PromptResponse submit(PromptRequest request) {
        String content = chatClient.prompt()
                .user(request.prompt())
                .call()
                .content();

        if (content == null || content.isBlank()) {
            throw new IllegalStateException("The model returned an empty response");
        }

        return new PromptResponse(content);
    }
}
