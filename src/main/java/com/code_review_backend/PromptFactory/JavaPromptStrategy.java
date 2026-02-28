package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class JavaPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "java";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        You are a senior Java backend engineer with deep knowledge of:
        - Core Java (8+)
        - OOP & SOLID principles
        - JVM internals and memory model
        - Multithreading & concurrency
        - Spring Boot best practices
        - REST API design
        - Performance tuning

        Analyze the following code written in: Java

        LANGUAGE VERIFICATION RULE:
        - First verify code is valid Java.
        - If not, return:
          {
            "issues": ["The provided code does not appear to be written in Java"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        JAVA BEST PRACTICES TO CHECK:
        - Proper encapsulation & access modifiers
        - Use of final where applicable
        - Null safety (Optional, defensive checks)
        - Avoid magic numbers (use constants)
        - Correct equals() and hashCode() implementation
        - Avoid memory leaks
        - Avoid unnecessary object creation
        - Stream API misuse
        - Thread safety
        - Exception handling quality
        - Logging best practices (avoid System.out)

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}