package com.code_review_backend.PromptFactory;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PromptFactory {

    private final Map<String, PromptStrategy> strategyMap;

    // Spring automatically injects all beans implementing PromptStrategy into this list
    public PromptFactory(List<PromptStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getLanguage().toLowerCase(),
                        strategy -> strategy
                ));
    }

    public PromptStrategy getStrategy(String language) {
        if (language == null || language.isBlank()) {
            // In a real API, you might return a "Generic" strategy instead of throwing an error
            throw new IllegalArgumentException("Language must be provided");
        }

        PromptStrategy strategy = strategyMap.get(language.toLowerCase());

        if (strategy == null) {
            // Providing a list of supported languages in the error helps the frontend display better messages
            String supported = String.join(", ", strategyMap.keySet());
            throw new IllegalArgumentException("Unsupported language: " + language + ". Supported: [" + supported + "]");
        }

        return strategy;
    }
}