package dev.mehdi.springai.prompt;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@EnableWireMock
@SpringBootTest(properties = {
    "spring.ai.openai.base-url=${wiremock.server.baseUrl}/v1",
    "spring.ai.openai.api-key=test-key",
    "spring.ai.openai.chat.model=gpt-5.6-luna"
})
class SpringAiPromptServiceWireMockTests {

    @InjectWireMock
    private WireMockServer wireMock;

    @Value("classpath:/test-prompt-response.json")
    private Resource responseResource;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @BeforeEach
    void setUp() throws IOException {
        String response = responseResource.getContentAsString(UTF_8);

        wireMock.stubFor(post(urlEqualTo("/v1/chat/completions"))
                .withRequestBody(equalToJson("""
                        {
                          "model": "gpt-5.6-luna",
                          "messages": [
                            {
                              "role": "user",
                              "content": "Explain why idempotency matters in distributed systems in one sentence."
                            }
                          ]
                        }
                        """, true, true))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)));
    }

    @Test
    void mapsTheProviderContentToAPromptResponse() {
        var service = new SpringAiPromptService(chatClientBuilder);

        PromptResponse result = service.submit(
                new PromptRequest(
                        "Explain why idempotency matters in distributed systems in one sentence."));

        assertThat(result).isNotNull();
        assertThat(result.response()).isEqualTo(
                "Idempotency allows repeated operations to produce the same effect, "
                        + "making retries safe when network failures occur.");
    }
}
