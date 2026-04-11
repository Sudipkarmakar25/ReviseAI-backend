package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewResponse;
import com.code_review_backend.provider.AIProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AIOrchestrator {

    private final AIProvider groq;
    private final ResponseParser responseParser;

    public AIOrchestrator(
            @Qualifier("groqProvider") AIProvider groq,
            ResponseParser responseParser) {
        this.groq = groq;
        this.responseParser = responseParser;
    }

    /**
     * Phase 1: FAST PATH
     * Directly calls Groq to get a response within ~1-2 seconds.
     */
    public ReviewResponse getFastGroqReview(String prompt) {
        log.info("⚡ Executing Phase 1: Instant Groq Review...");
        try {
            String rawResponse = groq.generateReview(prompt);
            return responseParser.parseLLMResponse(rawResponse);
        } catch (Exception e) {
            log.error("❌ Phase 1 (Groq) Failed: {}", e.getMessage());
            return fallbackResponse("Quick review unavailable. Deep analysis is running...");
        }
    }

    private ReviewResponse fallbackResponse(String message) {
        return new ReviewResponse(List.of(message), List.of(), "");
    }
}