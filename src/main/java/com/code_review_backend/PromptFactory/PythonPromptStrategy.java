package com.code_review_backend.PromptFactory;
import org.springframework.stereotype.Component;

@Component
public class PythonPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "python";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        You are a Senior Python Architect (Staff Level). Review the provided Python code for PEP8, security (OWASP), and O-Notation complexity.

        STRICT OUTPUT FORMAT:
        - Return ONLY a valid JSON object.
        - NEVER include markdown fences (e.g., ```json).
        - Use these keys: "issues", "suggestions", "refactorSnippet".

        CRITICAL JSON-SAFE CODING RULES:
        1. For "refactorSnippet", you must escape ALL double quotes with a backslash (\\").
        2. Use ONLY single quotes (') for string literals inside the Python code to avoid JSON conflicts.
        3. DO NOT use triple quotes (\"\"\" or ''') inside the refactorSnippet. Use standard strings with \\n for newlines.
        4. Represent newlines as the literal character sequence '\\n'.

        PYTHON SPECIFIC FOCUS:
        - Check for mutable default arguments (e.g., def func(a=[])).
        - Look for bare 'except:' clauses.
        - Ensure proper use of context managers (with statement).
        - Identify inefficient loops that could be list comprehensions or vectorized (NumPy style).

        CODE TO REVIEW:
        %s
        """.formatted(code);
    }
}