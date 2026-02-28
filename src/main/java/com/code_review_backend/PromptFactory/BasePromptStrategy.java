package com.code_review_backend.PromptFactory;

public abstract class BasePromptStrategy implements PromptStrategy {

    protected String jsonRules() {
        return """
        STRICT OUTPUT REQUIREMENTS:

        1. You MUST return ONLY valid JSON.
        2. Do NOT include explanations, markdown, code fences, comments, or extra text.
        3. Output must be a SINGLE valid JSON object.
        4. JSON must strictly follow this schema:

        {
          "issues": [],
          "suggestions": [],
          "refactorSnippet": ""
        }

        ----------------------------------------
        CRITICAL PRIORITY RULES:

        - Syntax errors and compile-time errors MUST be detected first.
        - Missing braces, missing semicolons, invalid declarations, or invalid structure
          MUST appear in "issues".
        - If syntax errors exist, focus primarily on fixing them.

        ----------------------------------------
        ISSUE RULES:

        - "issues" must contain ONLY real, verifiable problems.
        - Do NOT invent problems.
        - Do NOT give stylistic advice inside "issues".
        - If no real issues exist → return:
          ["No real issues found"]

        ----------------------------------------
        SUGGESTION RULES:

        - Suggestions must be optional improvements.
        - Keep them short and actionable.
        - Do NOT repeat issues as suggestions.
        -Donot give logger or documentation or comments as suggestions.

        ----------------------------------------
        REFACTOR RULES:

        - "refactorSnippet" must contain ONLY improved code.
        - No explanations.
        - No markdown.
        - If no improvement is required → return original code EXACTLY.
        - If syntax errors exist → return corrected working version.

        ----------------------------------------
        VALIDATION RULE:

        - Ensure final JSON is syntactically valid.
        - Ensure all fields exist.
        - Never omit required keys.
        """;
    }
}