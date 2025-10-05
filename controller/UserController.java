package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.Profile;
import com.hashedin.huspark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'INSTRUCTOR')")
    @GetMapping("/view-profile")
    public Profile getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return userService.getProfile(token);
    }

}
