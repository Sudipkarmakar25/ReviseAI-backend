package com.code_review_backend.PromptFactory;

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
        ### ROLE
        You are an expert Systems Programmer and Security Auditor specializing in C.

        ### TASK
        Analyze the provided C code for security vulnerabilities, memory leaks, and logic flaws.

        ### LANGUAGE VERIFICATION
        - If the code is not C, return only the "issues" field with a language mismatch message.

        ### C-SPECIFIC AUDIT RULES:
        - Memory Management: Check for 'malloc' without 'free', double-frees, and uninitialized pointers.
        - Security: Identify potential buffer overflows (e.g., use of 'gets' or unsafe 'scanf'), format string vulnerabilities, and integer overflows.
        - Performance: Identify redundant loops. Suggest optimal Time Complexity (O-notation) for data manipulation.
        - Pointer Safety: Ensure NULL checks before dereferencing pointers.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable C program.
        - Include all necessary headers (e.g., #include <stdio.h>, <stdlib.h>, <string.h>).
        - Must include an 'int main()' function so it can be compiled and executed immediately by Judge0.
        - Ensure proper use of semicolons and curly braces.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}