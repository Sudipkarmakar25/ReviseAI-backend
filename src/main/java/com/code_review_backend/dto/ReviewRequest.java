package com.code_review_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ReviewRequest {

    @NotBlank(message = "Code cannot be empty")
    private String code;

    @NotBlank(message = "Language must be specified")
    private String language;

    public ReviewRequest() {
    }

    public ReviewRequest(String code, String language) {
        this.code = code;
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }
}