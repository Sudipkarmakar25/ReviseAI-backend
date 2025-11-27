package com.code_review_backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeminiClient {

    @Value("${GROQ_API_KEY}")
    private String apiKey;

    // Groq model fallback list (fast → powerful)
    private final String[] MODEL_LIST = {
            "llama-3.1-8b-instant",
            "llama3-70b-8192",
            "mixtral-8x7b-32768"
    };

    private String workingModel = null;
    private final RestTemplate rest = new RestTemplate();

    /**
     * Detect the first working Groq model
     */
    private void detectWorkingModel() {
        if (workingModel != null) return;

        String url = "https://api.groq.com/openai/v1/chat/completions";

        for (String model : MODEL_LIST) {
            try {
                String body = """
                {
                  "model": "%s",
                  "messages": [
                    { "role": "user", "content": "health_check" }
                  ]
                }
                """.formatted(model);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(apiKey);

                HttpEntity<String> request = new HttpEntity<>(body, headers);

                ResponseEntity<String> response =
                        rest.exchange(url, HttpMethod.POST, request, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    workingModel = model;
                    System.out.println("✅ Groq model selected: " + workingModel);
                    return;
                }

            } catch (Exception ignored) {}
        }

        throw new RuntimeException("❌ No working Groq model found. Check your GROQ_API_KEY.");
    }

    /**
     * Main API call – keeps method name SAME for compatibility
     */
    public String generateReview(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("❌ GROQ_API_KEY missing in environment/application.properties");
        }

        detectWorkingModel();

        String url = "https://api.groq.com/openai/v1/chat/completions";

        // FULL JSON ESCAPING (Fixes your 400 Bad Request error)
        String safePrompt = prompt
                .replace("\\", "\\\\")   // escape backslash FIRST
                .replace("\"", "\\\"")   // escape quotes
                .replace("\n", "\\n")    // escape newline
                .replace("\r", "\\r")    // escape carriage return
                .replace("\t", "\\t");   // escape tabs

        String body = """
        {
          "model": "%s",
          "messages": [
            { "role": "user", "content": "%s" }
          ],
          "temperature": 0.2
        }
        """.formatted(workingModel, safePrompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return response.getBody(); // full JSON response

        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Groq request failed using model '" + workingModel + "' — " + e.getMessage(),
                    e
            );
        }
    }
}
