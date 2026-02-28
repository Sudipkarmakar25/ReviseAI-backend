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
        You are a senior JavaScript engineer with expertise in:
        - ES6+
        - Node.js
        - Async/Await
        - Security best practices
        - Clean code architecture

        Analyze the following code written in: JavaScript

        JAVASCRIPT BEST PRACTICES:
        - Use const/let (avoid var)
        - Avoid callback hell
        - Proper async/await usage
        - Error handling with try/catch
        - Avoid global scope pollution
        - Avoid blocking operations
        - Validate user input
        - Prevent memory leaks

        %s

        CODE:
        %s
        """.formatted(jsonRules(), code);
    }
}