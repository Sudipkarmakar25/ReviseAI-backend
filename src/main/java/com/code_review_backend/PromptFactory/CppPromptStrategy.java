package com.code_review_backend.PromptFactory;

import com.code_review_backend.PromptFactory.BasePromptStrategy;
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
        You are a senior C++ engineer (C++17+) with expertise in:
        - STL
        - RAII
        - Move semantics
        - Memory safety
        - Performance optimization

        Analyze the following code written in: C++

        LANGUAGE VERIFICATION RULE:
        - If not valid C++:
          {
            "issues": ["The provided code does not appear to be written in C++"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        C++ BEST PRACTICES:
        - Follow Rule of 3/5
        - Prefer smart pointers over raw pointers
        - Ensure const correctness
        - Avoid memory leaks
        - Avoid unnecessary copies
        - Use move semantics where applicable
        - Prevent integer overflow
        - Check boundary conditions
        - Avoid undefined behavior
        - Prefer STL algorithms over manual loops

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}