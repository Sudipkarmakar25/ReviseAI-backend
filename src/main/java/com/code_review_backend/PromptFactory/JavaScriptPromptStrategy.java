package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class JavaScriptPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "javascript";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        ### ROLE
        You are a Staff JavaScript Engineer specializing in Node.js performance, ES6+ standards, and Secure Coding.

        ### TASK
        Analyze the provided JavaScript code for asynchronous pitfalls, logic errors, and modern best practices.

        ### LANGUAGE VERIFICATION
        - Verify if this is JavaScript. If not, return only the "issues" field with a language mismatch message.

        ### JAVASCRIPT-SPECIFIC REVIEW RULES:
        - Asynchronous Logic: Check for unhandled Promises, "floating" async calls, and proper use of try/catch in async functions.
        - Performance: Identify inefficient array methods (e.g., nesting .map() and .filter() unnecessarily). Always suggest better Time Complexity (O-notation) for loops and data processing.
        - Modern Standards: Enforce ES6+ (arrow functions, destructuring, template literals). Check for 'var' usage (suggest 'let/const').
        - Safety: Identify potential prototype pollution, insecure regex, or "eval()" usage.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable Node.js script.
        - Use 'console.log()' for outputting results so it is visible in the Judge0 terminal.
        - Include any necessary logic to make the script execute immediately upon being called.
        - Ensure all template literals, braces, and parentheses are properly closed.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}