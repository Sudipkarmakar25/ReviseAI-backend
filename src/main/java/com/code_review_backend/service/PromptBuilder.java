package com.code_review_backend.service;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildPrompt(String code, String language) {
        return """
            You are a senior software engineer.

            Analyze the following code written in: %s

            STRICT RULES:
            - Return ONLY valid JSON.
            - Do NOT include ANY text before or after the JSON.
            - Do NOT include code fences like ```json or ``` or backticks.
            - Do NOT wrap JSON in markdown.
            - Do NOT add explanations, titles or commentary.
            - Output must be a SINGLE VALID JSON OBJECT EXACTLY in this format:

            {
              "issues": [],
              "suggestions": [],
              "refactorSnippet": ""
            }

            ADDITIONAL LANGUAGE CHECK RULE:
            - FIRST, verify if the provided code truly matches the declared language ("%s").
            - If the code does NOT match the declared language:
                {
                  "issues": ["The provided code does not appear to be written in %s"],
                  "suggestions": [],
                  "refactorSnippet": ""
                }
              And STOP. Do NOT analyze anything else.

            RULES FOR EACH FIELD:
            - "issues": real syntactic, logical or runtime issues ONLY.
              If no issues → ["No real issues found"].
            
            - "suggestions": only short, practical improvements.
              If no improvements → [].

            - "refactorSnippet":
                * MUST contain ONLY the improved code (NO labels, NO JSON, NO markdown).
                * Provide code ONLY if optimization or readability improves.
                * If no refactor is needed → return the original code EXACTLY as given.
                * ABSOLUTELY DO NOT include fields like "originalCode", "refactoredCode", or any other structure.

            Now analyze this code and produce ONLY the required JSON:

            CODE:
            %s
            """.formatted(language, language, language, code);
    }
}
