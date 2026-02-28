package com.code_review_backend.PromptFactory;

import com.code_review_backend.PromptFactory.PromptStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PromptFactory {

    private final Map<String, PromptStrategy> strategyMap;

    public PromptFactory(List<PromptStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getLanguage().toLowerCase(),
                        strategy -> strategy
                ));
    }

    public PromptStrategy getStrategy(String language) {
        if (language == null) {
            throw new IllegalArgumentException("Language cannot be null");
        }

        PromptStrategy strategy = strategyMap.get(language.toLowerCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }

        return strategy;
    }
}