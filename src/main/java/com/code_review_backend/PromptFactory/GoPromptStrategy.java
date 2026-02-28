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
        You are a senior Go backend engineer with expertise in:
        - Concurrency (goroutines, channels)
        - Memory efficiency
        - Clean API design
        - Idiomatic Go patterns

        Analyze the following code written in: Go

        LANGUAGE VERIFICATION RULE:
        - If not valid Go:
          {
            "issues": ["The provided code does not appear to be written in Go"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        GO BEST PRACTICES:
        - Always handle errors explicitly
        - Avoid goroutine leaks
        - Proper channel closing
        - Avoid global mutable state
        - Use context for cancellations
        - Avoid race conditions
        - Prefer composition over inheritance
        - Keep functions small and focused

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}