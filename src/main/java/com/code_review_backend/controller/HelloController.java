package com.code_review_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {

    @GetMapping("/test")
    public String testEnpoint() {
        return "hello";
    }
}
