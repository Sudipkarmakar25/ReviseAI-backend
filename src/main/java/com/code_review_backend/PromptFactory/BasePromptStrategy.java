package com.code_review_backend.PromptFactory;

public abstract class BasePromptStrategy implements PromptStrategy {

    protected String jsonRules() {
        return """
        OUTPUT REQUIREMENTS:

        - Return ONLY valid JSON.
        - Do NOT include explanations, markdown, code fences, or extra text.
        - Output must be a SINGLE JSON object.
        - JSON MUST strictly follow this exact schema:

        {
          "issues": [],
          "suggestions": [],
          "refactorSnippet": ""
        }

        --------------------------------------------------

        PRIORITY ORDER (MANDATORY):

        1. Syntax errors
        2. Compile-time errors
        3. Runtime errors
        4. Logical bugs
        5. Minor improvements (only if truly necessary)

        If higher-priority issues exist, focus on them first.

        --------------------------------------------------

        ISSUE RULES:

        - "issues" must include ONLY real, verifiable problems.
        - Do NOT invent issues.
        - Do NOT assume production context.
        - Do NOT include stylistic preferences.
        - Do NOT suggest logging frameworks, documentation, or comments.
        - If the code is syntactically and logically correct,
          return exactly:
            ["No real issues found"]

        --------------------------------------------------

        SUGGESTION RULES:

        - Suggestions are optional.
        - Provide suggestions ONLY if meaningful.
        - Keep suggestions short and practical.
        - Do NOT repeat issues as suggestions.
        - Do NOT over-engineer simple examples.
        - If no meaningful suggestions exist → return empty list.

        --------------------------------------------------

        REFACTOR RULES:

        - "refactorSnippet" must contain ONLY code.
        - No explanations.
        - No markdown.
        - If syntax errors exist → return corrected working code.
        - If no changes are required → return original code EXACTLY.
        - Do NOT modify code unnecessarily.

        --------------------------------------------------

        FINAL VALIDATION:

        - Ensure JSON is valid.
        - Ensure all keys exist.
        - Never omit required fields.
        - Never return null values.
        -If the code is syntactically and logically correct,
          return exactly:
            ["No real issues found"]
        """;
    }
}