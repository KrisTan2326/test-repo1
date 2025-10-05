package com.socio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platform")
@Tag(name = "1. Basic Platform", description = "Basic platform endpoints")
public class BasicPlatformController {

    @GetMapping("/status")
    @Operation(summary = "Check API Status", description = "A simple test endpoint to verify the application is running.")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("SOCIO Platform is up and running!");
    }
}