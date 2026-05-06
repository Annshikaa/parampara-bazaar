package com.parampara.bazaar.bargain.chatbot.client;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Primary
public class OpenAIClient implements AIClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAIClient.class);

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getChatCompletion(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("OpenAI API key is missing; chatbot will fall back to canned replies.");
            return "";
        }

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(url, request, Map.class);
        } catch (Exception ex) {
            log.warn("OpenAI request failed: {}", ex.getMessage());
            return "";
        }

        if (response.getBody() == null) return "";

        List choices = (List) response.getBody().get("choices");
        if (choices == null || choices.isEmpty()) return "";

        Map first = (Map) choices.get(0);
        Map message = (Map) first.get("message");
        if (message == null) return "";

        Object content = message.get("content");
        return content == null ? "" : content.toString();
    }
}
