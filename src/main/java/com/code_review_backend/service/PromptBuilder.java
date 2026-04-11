package com.code_review_backend.service;

import com.code_review_backend.PromptFactory.PromptFactory;
import com.code_review_backend.PromptFactory.PromptStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptBuilder {
    private final PromptFactory promptFactory;

    public String buildPrompt(String code, String language) {
        if (code == null || language == null) throw new IllegalArgumentException("Inputs missing");
        String normalizedLang = language.trim().toLowerCase();
        log.info("Building prompt for: {}", normalizedLang);
        PromptStrategy strategy = promptFactory.getStrategy(normalizedLang);
        return strategy.buildPrompt(code);
    }
}