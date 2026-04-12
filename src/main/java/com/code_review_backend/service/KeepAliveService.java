package com.code_review_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KeepAliveService {

    private final RestTemplate restTemplate = new RestTemplate();


    @Scheduled(fixedRate = 600000)
    public void keepAlive() {
        try {
            String url = "https://reviseai-backend-c2c2.onrender.com/api/health";
            restTemplate.getForObject(url, String.class);
            log.info("Self-ping successful. Server staying awake.");
        } catch (Exception e) {
            log.warn("Self-ping failed, but server is likely already awake.");
        }
    }
}