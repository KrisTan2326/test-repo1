package com.socio.controller;

import com.socio.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "4. Admin Operations", description = "APIs for administrative tasks")
public class AdminController {

    @PostMapping("/users/import")
    public ResponseEntity<String> bulkImportUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please supply a file.");
        }
        // In a real app, you would parse the CSV/XLSX file here
        try {
            long size = file.getSize();
            String contentType = file.getContentType();
            return ResponseEntity.ok("File '" + file.getOriginalFilename() + "' received. Size: " + size + ", Type: " + contentType + ". Processing would happen here.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process file.");
        }
    }
    
    @GetMapping("/stats/users")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        // This is a placeholder for real stats.
        // For example, count users by role.
        long userCount = UserController.userStore.values().stream()
            .filter(u -> "USER".equals(u.getRole()))
            .count();
        return ResponseEntity.ok(Map.of("totalUsers", userCount));
    }
}