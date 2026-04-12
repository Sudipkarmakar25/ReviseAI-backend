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
        ### ROLE
        You are a Staff TypeScript Engineer specializing in Type-Level Programming, Strict Type Safety, and Scalable Node.js architectures.

        ### TASK
        Analyze the provided TypeScript code for type-safety gaps, asynchronous errors, and structural integrity.

        ### LANGUAGE VERIFICATION
        - Verify if the code is TypeScript. If it's plain JavaScript without types, suggest adding type definitions in the "suggestions" field.

        ### TYPESCRIPT-SPECIFIC REVIEW RULES:
        - Type Safety: Flag usage of 'any'. Suggest 'unknown', 'never', or specific Interfaces/Types.
        - Advanced Patterns: Identify opportunities to use Generics, Utility Types (e.g., Partial, Pick, Omit), and Discriminated Unions.
        - Performance: Identify inefficient loops and suggests optimal Time Complexity (O-notation). Suggest 'Readonly' collections to prevent accidental mutations.
        - Modern Standards: Enforce strict null checks and identify unsafe type assertions ('as' keyword).

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable TypeScript script.
        - Use 'console.log()' for output results.
        - Include all necessary type definitions and interfaces within the snippet.
        - Ensure the code is compatible with 'ts-node' or standard TypeScript execution environments.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}