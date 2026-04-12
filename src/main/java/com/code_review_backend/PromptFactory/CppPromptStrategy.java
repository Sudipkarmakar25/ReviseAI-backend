package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;

@Component
public class CppPromptStrategy extends BasePromptStrategy {

    @Override
    public String getLanguage() {
        return "cpp";
    }

    @Override
    public String buildPrompt(String code) {
        return """
        ### ROLE
        You are a Senior C++ Performance Engineer (C++17/20).

        ### TASK
        Analyze the following C++ code for memory safety, logic errors, and time complexity.

        ### LANGUAGE VERIFICATION
        - If the code is not C++, return only the "issues" field stating the language mismatch.

        ### C++ SPECIFIC REVIEW RULES:
        - Memory: Check for leaks, raw pointer usage (prefer smart pointers), and RAII.
        - Performance: Identify bottlenecks. Always suggest better Time Complexity (e.g., O(n log n) vs O(n²)) if applicable.
        - Safety: Check for undefined behavior, buffer overflows, and iterator invalidation.
        - Modernity: Suggest STL algorithms, 'auto' keyword, and 'const' correctness.

        ### JUDGE0 REFACTOR REQUIREMENTS:
        - The 'refactorSnippet' MUST be a COMPLETE, standalone, and runnable C++ program.
        - Include all necessary #include directives (e.g., <iostream>, <vector>, <algorithm>).
        - Must include an 'int main()' function so it can be executed immediately by Judge0.
        - Ensure all braces and semicolons are perfectly balanced.

        --------------------------------------------------
        %s
        --------------------------------------------------

        CODE TO REVIEW:
        %s
        """.formatted(jsonRules(), code);
    }
}