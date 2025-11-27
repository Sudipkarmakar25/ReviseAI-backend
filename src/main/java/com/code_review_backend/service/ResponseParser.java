package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseParser {

    public ReviewResponse parseGeminiResponse(String aiResponse) {

        try {
            // -------- STEP 1: Extract LLM content safely --------
            String content = extractGroqContent(aiResponse);

            // -------- STEP 2: Clean markdown / fences --------
            content = sanitizeContent(content);

            // -------- STEP 3: Extract & auto-repair JSON --------
            String jsonText = extractAndFixJson(content);

            // -------- STEP 4: Parse JSON --------
            JSONObject parsed = new JSONObject(jsonText);

            List<String> issues = extractList(parsed, "issues");
            List<String> suggestions = extractList(parsed, "suggestions");
            String refactorSnippet = parsed.optString("refactorSnippet", "");

            return new ReviewResponse(issues, suggestions, refactorSnippet);
        }
        catch (Exception e) {

            // -------- SAFE FALLBACK --------
            List<String> issues = new ArrayList<>();
            issues.add("Unable to process response. Please review your code.");

            return new ReviewResponse(
                    issues,
                    new ArrayList<>(),
                    ""
            );
        }
    }

    // ------------ STEP 1: Extract content ------------

    private String extractGroqContent(String aiResponse) {
        try {
            JSONObject root = new JSONObject(aiResponse);
            return root
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .optString("content", "");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Groq content: " + aiResponse);
        }
    }

    // ------------ STEP 2: Sanitize text ------------

    private String sanitizeContent(String text) {
        return text
                .replace("```json", "")
                .replace("```", "")
                .replace("`", "")
                .trim();
    }

    // ------------ STEP 3: Extract + auto-fix JSON ------------

    private String extractAndFixJson(String text) {

        int start = text.indexOf('{');
        if (start == -1) {
            throw new RuntimeException("Missing JSON start: " + text);
        }

        String candidate = text.substring(start).trim();

        if (isValidJson(candidate)) return candidate;

        if (isValidJson(candidate + "}")) return candidate + "}";

        String noTrailingCommas = candidate
                .replaceAll(",\\s*}", "}")
                .replaceAll(",\\s*]", "]");
        if (isValidJson(noTrailingCommas)) return noTrailingCommas;

        throw new RuntimeException("JSON cannot be fixed: " + text);
    }

    private boolean isValidJson(String s) {
        try {
            new JSONObject(s);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    // ------------ STEP 4: Extract list fields ------------

    private List<String> extractList(JSONObject json, String key) {
        List<String> list = new ArrayList<>();

        if (!json.has(key)) return list;

        Object value = json.get(key);

        if (value instanceof JSONArray arr) {
            for (Object o : arr) list.add(String.valueOf(o));
        } else {
            list.add(String.valueOf(value));
        }

        return list;
    }
}
