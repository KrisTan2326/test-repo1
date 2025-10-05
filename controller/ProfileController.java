package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.Profile;
import com.hashedin.huspark.model.Users;
import com.hashedin.huspark.repository.ProfileRepository;
import com.hashedin.huspark.repository.UserRepository;
import com.hashedin.huspark.service.JWTService;
import com.hashedin.huspark.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @PostMapping("/profile")
    public ResponseEntity<Profile> createProfileForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody Profile profile) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long currentUserId = userRepository.findByUsername(username).getUserid();

//        if(!currentUserId.equals(userId)) {
//        } else {
//            throw new RuntimeException("You are not authorized to modify this profile.");
//        }

        Profile createdProfile = profileService.createProfileForUser(currentUserId, profile);
        return ResponseEntity.ok(createdProfile);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @PatchMapping("/profile/{profileId}")
    public ResponseEntity<Profile> patchProfile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable Long profileId,
            @RequestBody Profile profileDetails) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long currentUserId = userRepository.findByUsername(username).getUserid();
        System.out.println("current user is" + currentUserId);

        Optional<Profile> profile = Optional.ofNullable(
                profileRepository.findById(profileId)
                        .orElseThrow(() -> new RuntimeException("Profile Not Found"))
        );

        Long profileOwnerId = profile
                .map(Profile::getUser)
                .map(Users::getUserid)
                .orElse(null);
        if(!currentUserId.equals(profileOwnerId)) {
        } else {
            throw new RuntimeException("You are not authorized to modify this profile.");
        }

        Profile updatedProfile = profileService.patchProfile(profileId, profileDetails);
        return ResponseEntity.ok(updatedProfile);
    }
}
