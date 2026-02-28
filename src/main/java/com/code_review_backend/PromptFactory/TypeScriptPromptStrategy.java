package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class TypeScriptPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "typescript";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        You are a senior TypeScript engineer with deep expertise in:
        - TypeScript strict mode
        - Type safety & generics
        - Node.js ecosystem
        - Clean architecture
        - Runtime validation

        Analyze the following code written in: TypeScript

        LANGUAGE VERIFICATION RULE:
        - Verify code is valid TypeScript.
        - If not:
          {
            "issues": ["The provided code does not appear to be written in TypeScript"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        TYPESCRIPT BEST PRACTICES:
        - Avoid 'any'
        - Prefer interfaces for contracts
        - Use strict null checks
        - Avoid implicit any
        - Use readonly where possible
        - Proper generic constraints
        - Avoid unsafe type assertions
        - Ensure proper async error handling
        - Avoid mutation-heavy design

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}