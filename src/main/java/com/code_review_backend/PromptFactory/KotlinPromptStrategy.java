package com.code_review_backend.PromptFactory;

import com.code_review_backend.PromptFactory.BasePromptStrategy;
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
        You are a senior Kotlin engineer with expertise in:
        - Kotlin JVM
        - Null safety
        - Coroutines
        - Functional programming patterns

        Analyze the following code written in: Kotlin

        LANGUAGE VERIFICATION RULE:
        - If not valid Kotlin:
          {
            "issues": ["The provided code does not appear to be written in Kotlin"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        KOTLIN BEST PRACTICES:
        - Avoid excessive use of !!
        - Prefer val over var
        - Use data classes appropriately
        - Leverage sealed classes where suitable
        - Avoid Java-style patterns in Kotlin
        - Use coroutines properly
        - Ensure null safety
        - Prefer immutable collections

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}