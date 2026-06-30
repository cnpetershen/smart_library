package com.library.smart.client;

import com.library.smart.common.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeepSeekClient {

    private final WebClient webClient;

    public DeepSeekClient(@Value("${deepseek.api-key}") String apiKey,
                          @Value("${deepseek.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String chat(String systemPrompt, String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-chat",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7,
                "max_tokens", 2000
        );

        try {
            return webClient.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(30))
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                            .filter(this::isRetryableException))
                    .map(response -> parseAiResponse(response))
                    .onErrorResume(Exception.class, e -> {
                        log.error("DeepSeek API call failed: {}", e.getMessage());
                        return Mono.just(getFallbackMessage());
                    })
                    .block();
        } catch (Exception e) {
            log.error("DeepSeek API call failed: {}", e.getMessage());
            return getFallbackMessage();
        }
    }

    private boolean isRetryableException(Throwable e) {
        if (e instanceof WebClientResponseException wce) {
            int status = wce.getStatusCode().value();
            return status == HttpStatus.SERVICE_UNAVAILABLE.value()
                    || status == HttpStatus.TOO_MANY_REQUESTS.value()
                    || status == HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        return false;
    }

    private String parseAiResponse(Map response) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
            throw new BusinessException(500, "AI 响应格式异常");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Parse AI response failed: {}", e.getMessage());
            throw new BusinessException(500, "AI 响应解析失败");
        }
    }

    private String getFallbackMessage() {
        return "AI 分析服务暂时不可用，请稍后再试。如需紧急查看数据，请联系系统管理员。";
    }
}
