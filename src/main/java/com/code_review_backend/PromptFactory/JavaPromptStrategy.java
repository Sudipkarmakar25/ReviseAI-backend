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
        You are a Staff Level Java Engineer. Analyze the provided Java code for SOLID principles, JVM performance, and security.

        STRICT OUTPUT FORMAT:
        - Return ONLY a valid JSON object.
        - NO markdown formatting (DO NOT use ```json).
        - Keys: "issues", "suggestions", "refactorSnippet".

        CRITICAL JSON-SAFE CODING RULES:
        1. For the "refactorSnippet", you MUST escape all double quotes with a backslash (\\").
        2. Replace all newlines with the literal characters '\\n'.
        3. Ensure the result is a single-line JSON string.
        4. If the code provided is NOT Java (e.g., Python), state this in the 'issues' array but provide a review anyway.

        JAVA BEST PRACTICES FOCUS:
        - OOP & SOLID principles.
        - Proper use of Optionals and Null-safety.
        - Resource management (try-with-resources).
        - Inefficient object creation or collection usage.
        - Use of appropriate Access Modifiers.
        - Concurrency and synchronization issues.

        CODE TO REVIEW:
        %s
        """.formatted(code);
    }
}