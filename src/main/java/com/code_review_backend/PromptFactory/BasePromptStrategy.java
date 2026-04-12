package com.code_review_backend.PromptFactory;

public abstract class BasePromptStrategy implements PromptStrategy {

    protected String jsonRules() {
        return """
        ### CRITICAL OUTPUT RULES:
        1. Return ONLY a valid JSON object. No markdown code blocks (```json), no conversational text, and no explanations.
        2. Schema:
           {
             "issues": ["string"],
             "suggestions": ["string"],
             "refactorSnippet": "string"
           }

        ### PHASE 1: LANGUAGE VALIDATION
        - First, verify if the provided code matches the requested programming language.
        - If the code is written in a different language, return:
          {"issues": ["Incorrect language detected. This code does not appear to be the specified language."], "suggestions": [], "refactorSnippet": ""}
        - Only proceed to Phase 2 if the language is correct.

        ### PHASE 2: REVIEW GUIDELINES
        - ISSUES: Identify real bugs, syntax errors, or logical flaws. Do not invent problems. If perfect, return ["No real issues found"].
        - SUGGESTIONS: Focus on Time Complexity (e.g., O(n) vs O(n²)), memory efficiency, and language-specific best practices. Keep them concise.
        - REFACTOR SNIPPET: 
            * This MUST be a complete, standalone, and runnable program.
            * Include all necessary imports, headers, and a main entry point.
            * It must be formatted specifically to execute immediately in a Judge0 environment.
            * Ensure all brackets, parentheses, and quotes are properly closed.

        ### DATA INTEGRITY (STRICT):
        - "issues" and "suggestions" MUST be arrays of PLAIN TEXT STRINGS. 
        - PROHIBITED: Do not use nested JSON objects like {"description": "..."} inside arrays.
        - PROHIBITED: Do not use labels like "Category:" or "Priority:".
        - The entire response must be a single, flat JSON object.
        """;
    }
}