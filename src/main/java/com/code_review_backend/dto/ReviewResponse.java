package com.code_review_backend.dto;

import java.util.List;

public class ReviewResponse {
    private List<String> issues;
    private List<String> suggestions;
    private String refactorSnippet;

    public ReviewResponse(List<String> issues, List<String> suggestions, String refactorSnippet) {
        this.issues = issues;
        this.suggestions = suggestions;
        this.refactorSnippet = refactorSnippet;
    }

    public List<String> getIssues() { return issues; }
    public List<String> getSuggestions() { return suggestions; }
    public String getRefactorSnippet() { return refactorSnippet; }
}
