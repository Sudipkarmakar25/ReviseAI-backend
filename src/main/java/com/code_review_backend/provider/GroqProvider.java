package com.code_review_backend.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("groqProvider") // Bean name for Qualifier
public class GroqProvider implements AIProvider {

    @Value("${groq.api.key}")
    private String apiKey;

    private final String[] MODEL_LIST = {
            "llama-3.1-8b-instant",
            "llama3-70b-8192",
            "mixtral-8x7b-32768"
    };

    private String workingModel = null;
    private final RestTemplate rest = new RestTemplate();

    @Override
    public String generateReview(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("❌ groq.api.key missing");
        }
        detectWorkingModel();

        String url = "https://api.groq.com/openai/v1/chat/completions";
        String safePrompt = sanitizePrompt(prompt);

        String body = """
        {
          "model": "%s",
          "messages": [{ "role": "user", "content": "%s" }],
          "temperature": 0.2,
          "response_format": { "type": "json_object" }
        }
        """.formatted(workingModel, safePrompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        return response.getBody();
    }

    private void detectWorkingModel() {
        if (workingModel != null) return;
        String url = "https://api.groq.com/openai/v1/chat/completions";
        for (String model : MODEL_LIST) {
            try {
                String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"ping\"}]}";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(apiKey);
                if (rest.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class).getStatusCode().is2xxSuccessful()) {
                    workingModel = model;
                    return;
                }
            } catch (Exception ignored) {}
        }
    }

    private String sanitizePrompt(String prompt) {
        return prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    @Override public String getProviderName() { return "Groq"; }
}