package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import com.code_review_backend.provider.AIProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncRefinementService {

    private final AIProvider gemini;
    private final AIProvider groq;
    private final CacheService cacheService;
    private final ResponseParser parser;
    private final PromptBuilder promptBuilder;

    // Manual constructor to handle Qualifiers correctly
    public AsyncRefinementService(
            @Qualifier("geminiProvider") AIProvider gemini,
            @Qualifier("groqProvider") AIProvider groq,
            CacheService cacheService,
            ResponseParser parser,
            PromptBuilder promptBuilder) {
        this.gemini = gemini;
        this.groq = groq;
        this.cacheService = cacheService;
        this.parser = parser;
        this.promptBuilder = promptBuilder;
    }

    @Async
    public void refineAndCache(ReviewRequest request, ReviewResponse groqInitial, String cacheKey) {
        try {
            log.info("🚀 Background refinement started for key: {}", cacheKey);

            // 1. Get Gemini Response (Deep)
            String actualPrompt = promptBuilder.buildPrompt(request.getCode(), request.getLanguage());
            String geminiRaw = gemini.generateReview(actualPrompt);
            ReviewResponse geminiParsed = parser.parseLLMResponse(geminiRaw);

            // 2. Binary Judge Prompt
            // Inside the refineAndCache method
            String evaluationPrompt = String.format("""
    Compare these two code reviews and pick the winner. 
    You must provide your evaluation in a JSON format eventually, 
    but for this specific task, just analyze the quality.
    
    Review A: %s
    Review B: %s
    
    Decision Criteria: Which review identifies better bugs and provides a superior refactor snippet?
    Output ONLY 'A' or 'B'. 
    Note: This request is for a JSON-ready analysis.
    """, groqInitial.toString(), geminiParsed.toString());

            // 3. Evaluate and Store
            String decision = groq.generateReview(evaluationPrompt).toUpperCase();

            if (decision.contains("B")) {
                log.info("🏆 Gemini won evaluation.");
                cacheService.put(cacheKey, geminiParsed);
            } else {
                log.info("🏆 Groq maintained quality.");
                cacheService.put(cacheKey, groqInitial);
            }

        } catch (Exception e) {
            log.error("❌ Refinement error: {}", e.getMessage());
        }
    }
}