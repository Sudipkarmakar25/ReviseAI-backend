package com.code_review_backend.provider;

public interface AIProvider {
    String generateReview(String prompt);
    String getProviderName();
}