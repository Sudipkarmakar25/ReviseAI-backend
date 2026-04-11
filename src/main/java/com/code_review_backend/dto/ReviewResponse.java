package com.code_review_backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Serializable is required for Redis storage.
 */
public class ReviewResponse implements Serializable {

    private final List<String> issues;
    private final List<String> suggestions;
    private final String refactorSnippet;

    // Use @JsonCreator for immutable objects so Jackson can map the JSON fields
    // to this specific constructor during deserialization from Redis.
    @JsonCreator
    public ReviewResponse(
            @JsonProperty("issues") List<String> issues,
            @JsonProperty("suggestions") List<String> suggestions,
            @JsonProperty("refactorSnippet") String refactorSnippet
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

    // Fallback static method for error cases
    public static ReviewResponse empty() {
        return new ReviewResponse(List.of(), List.of(), "");
    }
}