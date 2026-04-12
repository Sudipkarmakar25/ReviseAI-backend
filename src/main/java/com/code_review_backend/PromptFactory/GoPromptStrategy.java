package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class GoPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "go";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        ### ROLE
        You are a Senior Go Backend Engineer specializing in high-performance distributed systems.

        ### TASK
        Analyze the provided Go code for idiomatic patterns, concurrency safety, and efficiency.

        ### LANGUAGE VERIFICATION
        - If the code is not Go, return only the "issues" field with a language mismatch message.

        ### GO-SPECIFIC REVIEW RULES:
        - Error Handling: Check for unhandled errors or improper use of 'panic'.
        - Concurrency: Identify race conditions, goroutine leaks, and improper channel usage.
        - Performance: Suggest optimizations for slice/map allocations. Always suggest better Time Complexity (O-notation) for algorithms.
        - Idioms: Prefer 'short variable declarations', proper 'defer' usage, and composition over inheritance.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable Go program.
        - Must start with 'package main'.
        - Include all necessary imports (e.g., import "fmt", "time", "sync").
        - Must include a 'func main()' as the entry point for Judge0 execution.
        - Ensure the code is properly formatted according to 'gofmt' standards.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}