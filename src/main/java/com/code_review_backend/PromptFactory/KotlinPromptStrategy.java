package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class KotlinPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "kotlin";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        ### ROLE
        You are a Senior Kotlin Engineer specializing in Kotlin Multiplatform, Coroutines, and Idiomatic Kotlin patterns.

        ### TASK
        Analyze the provided Kotlin code for safety, performance, and adherence to Kotlin's idiomatic style.

        ### LANGUAGE VERIFICATION
        - Verify if the code is Kotlin. If not, return only the "issues" field with a language mismatch message.

        ### KOTLIN-SPECIFIC REVIEW RULES:
        - Null Safety: Flag excessive use of the double-bang operator (!!). Suggest 'let', 'run', or Elvis operator (?:) instead.
        - Idioms: Identify Java-style patterns (like manual getters/setters or verbose loops). Suggest Data Classes, Extension Functions, or Scope Functions.
        - Concurrency: Check for improper Coroutine usage, such as blocking the main thread or not using 'suspend' correctly.
        - Performance: Suggest sequences over lists for large data sets. Always include Time Complexity (O-notation) suggestions for algorithmic logic.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable Kotlin program.
        - Must include a 'fun main(args: Array<String>)' or 'fun main()' entry point.
        - Include all necessary imports (e.g., import kotlinx.coroutines.*, import java.util.*).
        - Ensure the refactored code is idiomatic (e.g., using 'val' instead of 'var' wherever possible).

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}