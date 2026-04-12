package com.code_review_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> healthCheck() {
        // Return a simple JSON map
        return Map.of(
                "status", "UP",
                "message", "ReviseAI Server is active"
        );
    }
}