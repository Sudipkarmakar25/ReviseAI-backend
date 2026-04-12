package com.code_review_backend.controller;

import com.code_review_backend.dto.ReviewRequest;
import com.code_review_backend.dto.ReviewResponse;
import com.code_review_backend.service.ReviewStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/review/status")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReviewStatusController {

    private final ReviewStatusService statusService;

    @PostMapping
    public ResponseEntity<ReviewResponse> checkRefinedStatus(@RequestBody ReviewRequest request) {
        log.info("🔍 Received polling request for refined status. Language: {}", request.getLanguage());

        ReviewResponse refinedResult = statusService.getRefinedResult(request);

        if (refinedResult == null) {
            log.info("⏳ Refined result not yet available in Redis for this request.");
            return ResponseEntity.accepted().build();
        }

        log.info("✅ Refined result found! Sending to client.");
        return ResponseEntity.ok(refinedResult);
    }
}