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
        ### ROLE
        You are a Staff Level Java Engineer specializing in JVM performance, Spring Boot architecture, and Clean Code.

        ### TASK
        Analyze the provided Java code for logic flaws, SOLID principle violations, and efficiency.

        ### LANGUAGE VERIFICATION
        - First, verify if this is Java code. If not, return only the "issues" field with a language mismatch message.

        ### JAVA-SPECIFIC REVIEW RULES:
        - Performance: Identify inefficient object creation, improper collection usage (e.g., ArrayList vs LinkedList), and unnecessary synchronization.
        - Complexity: Always suggest better Time Complexity (O-notation) for algorithms and data manipulations.
        - Safety: Check for NullPointerExceptions (suggest Optional), unclosed resources (suggest try-with-resources), and thread-safety.
        - Standards: Ensure proper use of Access Modifiers, Records (for Java 14+), and Streams API where applicable.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable Java program.
        - The class name MUST be 'Main' to ensure compatibility with Judge0 execution.
        - Include all necessary imports (e.g., import java.util.*, java.util.stream.*).
        - Must include a 'public static void main(String[] args)' method.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}