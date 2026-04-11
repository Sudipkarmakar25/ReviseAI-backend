package com.code_review_backend.provider;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.ThinkingConfig; // Ensure this is imported
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("geminiProvider")
public class GeminiProvider implements AIProvider {

    private Client client;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("❌ Gemini API Key is missing in application.properties");
        }
        // Build the client once
        this.client = Client.builder().apiKey(apiKey).build();
    }

    @Override
    public String generateReview(String prompt) {
        try {
            // Build the thinking config to DISABLE overthinking
            ThinkingConfig thinkingConfig = ThinkingConfig.builder()
                    .includeThoughts(false)
                    .thinkingBudget(0) // 0 budget = Ultra-fast mode
                    .build();

            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseMimeType("application/json")
                    .thinkingConfig(thinkingConfig)
                    .build();

            // Use the absolute fastest model ID for April 2026
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-3.1-flash-lite-preview",
                    prompt,
                    config
            );

            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Gemini SDK Error: " + e.getMessage(), e);
        }
    }

    @Override
    public String getProviderName() {
        return "Gemini";
    }
}