package com.parampara.bazaar.bargain.chatbot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Component
public class OpenRouterClient implements AIClient {

    private static final Logger log = LoggerFactory.getLogger(OpenRouterClient.class);

    @Value("${openrouter.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getChatCompletion(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("OpenRouter API key is missing; chatbot will fall back to canned replies.");
            return "";
        }

        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", "mistralai/mistral-7b-instruct",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(url, request, Map.class);
        } catch (Exception ex) {
            log.warn("OpenRouter request failed: {}", ex.getMessage());
            return "";
        }

        if (response.getBody() == null) return "";

        List choices = (List) response.getBody().get("choices");
        if (choices == null || choices.isEmpty()) return "";

        Map first = (Map) choices.get(0);
        Map message = (Map) first.get("message");

        return message.get("content").toString();
    }
}
