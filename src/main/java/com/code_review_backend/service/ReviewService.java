package com.code_review_backend.service;

import com.code_review_backend.client.GeminiClient;
import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final PromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final ResponseParser responseParser;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    public ReviewService(
            PromptBuilder promptBuilder,
            GeminiClient geminiClient,
            ResponseParser responseParser,
            CacheService cacheService,
            ObjectMapper objectMapper
    ) {
        this.promptBuilder = promptBuilder;
        this.geminiClient = geminiClient;
        this.responseParser = responseParser;
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
    }

    public ReviewResponse processReview(ReviewRequest request) {

        validateRequest(request);

        log.info("Processing review for language: {}", request.getLanguage());


        String cacheKey = cacheService.generateKey(request.getLanguage(), request.getCode());
        Object cachedData = cacheService.get(cacheKey);

        if (cachedData != null) {
            log.info("Cache hit for language: {}. Skipping LLM call.", request.getLanguage());
            return objectMapper.convertValue(cachedData, ReviewResponse.class);
        }



        log.debug("Cache miss. Proceeding with LLM generation.");


        String prompt = promptBuilder.buildPrompt(
                request.getCode(),
                request.getLanguage()
        );


        String rawResponse = geminiClient.generateReview(prompt);


        ReviewResponse response = responseParser.parseLLMResponse(rawResponse);


        if (isResponseCacheable(response)) {
            cacheService.put(cacheKey, response);
            log.debug("Response saved to cache.");
        }

        log.info("Review processing completed successfully");
        return response;
    }


    private boolean isResponseCacheable(ReviewResponse response) {
        return response != null &&
                (response.getIssues() != null && !response.getIssues().isEmpty());
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