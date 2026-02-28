package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseParser {

    private static final Logger log = LoggerFactory.getLogger(ResponseParser.class);

    public ReviewResponse parseLLMResponse(String rawResponse) {

        if (rawResponse == null || rawResponse.isBlank()) {
            return fallback("Empty AI response received.");
        }

        try {
            String content = extractLLMContent(rawResponse);
            String sanitized = sanitizeContent(content);
            String validJson = extractAndRepairJson(sanitized);

            JSONObject json = new JSONObject(validJson);

            return new ReviewResponse(
                    extractList(json, "issues"),
                    extractList(json, "suggestions"),
                    json.optString("refactorSnippet", "")
            );

        } catch (Exception e) {
            log.error("Failed to parse AI response", e);
            return fallback("AI response could not be processed safely.");
        }
    }

    // ----------------------------------------
    // STEP 1: Extract LLM content
    // ----------------------------------------

    private String extractLLMContent(String rawResponse) {

        try {
            JSONObject root = new JSONObject(rawResponse);

            return root
                    .optJSONArray("choices")
                    .optJSONObject(0)
                    .optJSONObject("message")
                    .optString("content", "");

        } catch (Exception e) {
            throw new RuntimeException("Invalid LLM structure.");
        }
    }

    // ----------------------------------------
    // STEP 2: Sanitize markdown / fences
    // ----------------------------------------

    private String sanitizeContent(String text) {
        if (text == null) return "";

        return text
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .replaceAll("`", "")
                .trim();
    }

    // ----------------------------------------
    // STEP 3: Extract & Repair JSON
    // ----------------------------------------

    private String extractAndRepairJson(String text) {

        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');

        if (start == -1 || end == -1 || end <= start) {
            throw new RuntimeException("No valid JSON block found.");
        }

        String candidate = text.substring(start, end + 1);

        candidate = removeTrailingCommas(candidate);

        if (!isValidJson(candidate)) {
            throw new RuntimeException("Malformed JSON after repair attempt.");
        }

        return candidate;
    }

    private String removeTrailingCommas(String input) {
        return input
                .replaceAll(",\\s*}", "}")
                .replaceAll(",\\s*]", "]");
    }

    private boolean isValidJson(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    // ----------------------------------------
    // STEP 4: Extract list fields safely
    // ----------------------------------------

    private List<String> extractList(JSONObject json, String key) {

        List<String> result = new ArrayList<>();

        if (!json.has(key)) return result;

        Object value = json.get(key);

        if (value instanceof JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                result.add(String.valueOf(array.get(i)));
            }
        } else {
            result.add(String.valueOf(value));
        }

        return result;
    }


    private ReviewResponse fallback(String message) {

        List<String> issues = new ArrayList<>();
        issues.add(message);

        return new ReviewResponse(
                issues,
                new ArrayList<>(),
                ""
        );
    }
}