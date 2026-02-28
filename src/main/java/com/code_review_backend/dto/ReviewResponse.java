package com.code_review_backend.dto;

import java.util.Collections;
import java.util.List;

public class ReviewResponse {

    private final List<String> issues;
    private final List<String> suggestions;
    private final String refactorSnippet;

    public ReviewResponse(
            List<String> issues,
            List<String> suggestions,
            String refactorSnippet
    ) {
        this.issues = issues == null ? List.of() : Collections.unmodifiableList(issues);
        this.suggestions = suggestions == null ? List.of() : Collections.unmodifiableList(suggestions);
        this.refactorSnippet = refactorSnippet == null ? "" : refactorSnippet;
    }

    public List<String> getIssues() {
        return issues;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public String getRefactorSnippet() {
        return refactorSnippet;
    }
}