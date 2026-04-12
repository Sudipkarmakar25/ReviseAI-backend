package com.code_review_backend.service;

import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewStatusService {

    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    public ReviewResponse getRefinedResult(ReviewRequest request) {
        // Re-generate the key using the same logic as the main ReviewService
        String key = cacheService.generateKey(request.getLanguage(), request.getCode());
        log.info("Checking Redis for refinement update with key: {}", key);

        Object cachedData = cacheService.get(key);

        if (cachedData == null) {
            return null;
        }

        return objectMapper.convertValue(cachedData, ReviewResponse.class);
    }
}