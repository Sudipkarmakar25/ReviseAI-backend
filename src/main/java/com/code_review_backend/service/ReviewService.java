package com.code_review_backend.service;

import com.code_review_backend.client.GeminiClient;
import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private PromptBuilder promptBuilder;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private ResponseParser parser;

    public ReviewResponse processReview(ReviewRequest request) {

        // 1. Build Prompt
        String prompt = promptBuilder.buildPrompt(request.getCode(), request.getLanguage());

        // 2. Call Gemini API
        String rawResponse = geminiClient.generateReview(prompt);

        // 3. Parse Gemini JSON → DTO
        return parser.parseGeminiResponse(rawResponse);
    }
}
