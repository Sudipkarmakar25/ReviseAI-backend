package com.code_review_backend.PromptFactory;

import com.code_review_backend.PromptFactory.BasePromptStrategy;
import org.springframework.stereotype.Component;

@Component
public class CPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "c";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        You are a senior systems programmer specializing in C.

        Analyze the following code written in: C

        LANGUAGE VERIFICATION RULE:
        - If not valid C:
          {
            "issues": ["The provided code does not appear to be written in C"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        C BEST PRACTICES:
        - Prevent buffer overflow
        - Null pointer safety
        - Correct malloc/free pairing
        - Avoid memory leaks
        - Avoid undefined behavior
        - Validate array bounds
        - Prevent format string vulnerabilities
        - Proper use of const
        - Avoid uninitialized variables

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}