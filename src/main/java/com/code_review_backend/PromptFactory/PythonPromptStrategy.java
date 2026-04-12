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
        ### ROLE
        You are a Staff-Level Python Architect specializing in PEP 8, asynchronous programming, and high-performance data processing.

        ### TASK
        Analyze the provided Python code for logical errors, security vulnerabilities (OWASP), and algorithmic efficiency.

        ### LANGUAGE VERIFICATION
        - Verify if this is Python code. If not, return only the "issues" field with a language mismatch message.

        ### PYTHON-SPECIFIC REVIEW RULES:
        - Idioms: Check for mutable default arguments (e.g., def f(a=[])), bare 'except:' clauses, and improper scope usage.
        - Performance: Identify inefficient loops that should be list comprehensions or generators. Always suggest better Time Complexity (O-notation) where applicable.
        - Standards: Enforce PEP 8 naming conventions and suggest context managers ('with' statements) for resource handling.
        - Security: Look for unsafe 'eval()' or 'exec()' calls and improper handling of user input.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable Python script.
        - Include all necessary imports (e.g., import os, math, collections).
        - Use 'if __name__ == "__main__":' as the entry point to ensure execution safety in Judge0.
        - Ensure all indentation is perfectly consistent (4 spaces) and brackets are closed.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}