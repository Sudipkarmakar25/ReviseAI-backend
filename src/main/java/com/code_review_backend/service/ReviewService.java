package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewService {

    private final PromptBuilder promptBuilder;
    private final AIOrchestrator aiOrchestrator;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper;
    private final AsyncRefinementService refinementService;

    public ReviewService(PromptBuilder promptBuilder, AIOrchestrator aiOrchestrator,
                         CacheService cacheService, ObjectMapper objectMapper,
                         AsyncRefinementService refinementService) {
        this.promptBuilder = promptBuilder;
        this.aiOrchestrator = aiOrchestrator;
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
        this.refinementService = refinementService;
    }

    public ReviewResponse processReview(ReviewRequest request) {
        validateRequest(request);
        String cacheKey = cacheService.generateKey(request.getLanguage(), request.getCode());

        // 1. Check Cache
        Object cachedData = cacheService.get(cacheKey);
        if (cachedData != null) {
            log.info("🎯 Cache hit for {}", request.getLanguage());
            return objectMapper.convertValue(cachedData, ReviewResponse.class);
        }

        // 2. Cache Miss: Get IMMEDIATE fast response
        String prompt = promptBuilder.buildPrompt(request.getCode(), request.getLanguage());
        ReviewResponse fastResponse = aiOrchestrator.getFastGroqReview(prompt);

        // 3. Trigger ASYNC refinement (Passed 'request' object for syncing)
        refinementService.refineAndCache(request, fastResponse, cacheKey);

        // 4. Return fast response immediately
        return fastResponse;
    }

    private void validateRequest(ReviewRequest request) {
        if (request == null || request.getCode() == null || request.getLanguage() == null) {
            throw new IllegalArgumentException("Invalid Review Request: Code and Language required");
        }
    }
}