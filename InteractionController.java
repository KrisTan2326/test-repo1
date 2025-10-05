package com.socio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interactions")
public class InteractionController {

    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId) {
        // Implement logic to follow a user
        return ResponseEntity.ok("User followed successfully.");
    }

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId) {
        // Implement logic to unfollow a user
        return ResponseEntity.ok("User unfollowed successfully.");
    }
}