package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.Course;
import com.hashedin.huspark.model.Users;
import com.hashedin.huspark.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/enroll/{courseid}")
    public ResponseEntity<Users> enrollStudent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long courseid) {
        String token = authHeader.replace("Bearer ", "");
        return enrollmentService.enrollStudent(token, courseid);
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/markcomplete/course/{courseid}/subsection/{subsectionid}")
    public ResponseEntity<String> markComplete(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long courseid,
                                               @PathVariable Long subsectionid) {
        String token = authHeader.replace("Bearer ", "");
        return enrollmentService.markComplete(token, courseid, subsectionid);
    }



    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/get-enrolled-courses")
    public ResponseEntity<List<Course>> getAllCourses(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return enrollmentService.getAllCoursesById(token);
    }



}
