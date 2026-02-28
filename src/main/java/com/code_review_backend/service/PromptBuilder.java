package com.code_review_backend.service;

import com.code_review_backend.PromptFactory.PromptFactory;
import com.code_review_backend.PromptFactory.PromptStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilder {

    private static final Logger log = LoggerFactory.getLogger(PromptBuilder.class);

    private final PromptFactory promptFactory;

    @Autowired
    public PromptBuilder(PromptFactory promptFactory) {
        this.promptFactory = promptFactory;
    }

    public String buildPrompt(String code, String language) {

        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }

        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }

        String normalizedLang = language.trim().toLowerCase();

        log.info("Building prompt for language: {}", normalizedLang);

        PromptStrategy strategy = promptFactory.getStrategy(normalizedLang);

        return strategy.buildPrompt(code);
    }
}