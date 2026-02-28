package com.code_review_backend.service;

import com.code_review_backend.client.GeminiClient;
import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final PromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final ResponseParser responseParser;

    public ReviewService(
            PromptBuilder promptBuilder,
            GeminiClient geminiClient,
            ResponseParser responseParser
    ) {
        this.promptBuilder = promptBuilder;
        this.geminiClient = geminiClient;
        this.responseParser = responseParser;
    }

    public ReviewResponse processReview(ReviewRequest request) {

        validateRequest(request);

        log.info("Processing review for language: {}", request.getLanguage());

        // Step 1: Build Prompt
        String prompt = promptBuilder.buildPrompt(
                request.getCode(),
                request.getLanguage()
        );

        log.debug("Prompt generated successfully");

        // Step 2: Call LLM API
        String rawResponse = geminiClient.generateReview(prompt);

        log.debug("Raw response received from LLM");

        // Step 3: Parse LLM JSON → DTO
        ReviewResponse response = responseParser.parseLLMResponse(rawResponse);

        log.info("Review processing completed successfully");

        return response;
    }

    private void validateRequest(ReviewRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Review request cannot be null");
        }

        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new IllegalArgumentException("Code cannot be empty");
        }

        if (request.getLanguage() == null || request.getLanguage().isBlank()) {
            throw new IllegalArgumentException("Language must be specified");
        }
    }
}