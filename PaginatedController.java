package com.socio.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/paginated")
public class PaginatedController {

    @GetMapping("/items")
    public ResponseEntity<Object> getPaginatedItems(Pageable pageable) {
        // Implement logic to fetch items with pagination
        // The Pageable object will be automatically populated from request parameters like ?page=0&size=10&sort=name,asc
        return ResponseEntity.ok("Paginated list of items.");
    }
}