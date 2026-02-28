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
        You are a senior Python engineer with deep knowledge of:
        - Python 3.x
        - PEP8 standards
        - Async programming
        - Performance optimization
        - Clean architecture

        Analyze the following code written in: Python

        LANGUAGE VERIFICATION RULE:
        - Verify code is valid Python.
        - If not:
          {
            "issues": ["The provided code does not appear to be written in Python"],
            "suggestions": [],
            "refactorSnippet": ""
          }

        PYTHON BEST PRACTICES:
        - Proper indentation
        - Avoid mutable default arguments
        - Use list/dict comprehensions properly
        - Avoid global variables
        - Proper exception handling
        - Efficient data structures
        - Time/space complexity awareness
        - Use typing annotations where helpful

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}