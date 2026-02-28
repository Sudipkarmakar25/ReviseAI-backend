package com.code_review_backend.PromptFactory;

public interface PromptStrategy {

    String getLanguage();

    String buildPrompt(String code);

}