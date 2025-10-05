package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.Course;
import com.hashedin.huspark.model.Users;
import com.hashedin.huspark.repository.CourseRepository;
import com.hashedin.huspark.repository.UserRepository;
import com.hashedin.huspark.service.CourseService;
import com.hashedin.huspark.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instructor")
public class CourseController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping("/create-course")
    public ResponseEntity<Course> createCourse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @RequestBody Course course) {
        // Remove "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long instructorId = userRepository.findByUsername(username).getUserid();

        return ResponseEntity.ok(courseService.createCourse(instructorId, course));

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/all-courses")
    public ResponseEntity<List<Course>> getAllCourses() {

        return ResponseEntity.ok(courseService.getAllCourses());

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {

        return ResponseEntity.ok(courseService.getCourseById(id));

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long id) {
        System.out.println("Hello");
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long currentUserId = userRepository.findByUsername(username).getUserid();

        Optional<Course> course = Optional.ofNullable(courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not Found")));

        Long instructorId = course
                .map(Course::getInstructor)
                .map(Users::getUserid)
                .orElse(null);

        if (!instructorId.equals(currentUserId)) {
            return ResponseEntity.badRequest().body("You are not authorized to perform this action.");
        }



//        Long instructorId = course.getInstructor().getUserId();

        return ResponseEntity.ok(courseService.deleteCourse(id));


    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PatchMapping("/course/{courseid}")
    public ResponseEntity<Course> updateCourse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long courseid,
                                               @RequestBody Course course) {
//        System.out.println("Hello");
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long currentUserId = userRepository.findByUsername(username).getUserid();

        Optional<Course> oldCourse = Optional.ofNullable(courseRepository.findById(courseid)
                .orElseThrow(() -> new RuntimeException("Course Not Found")));

        Long instructorId = oldCourse
                .map(Course::getInstructor)
                .map(Users::getUserid)
                .orElse(null);

        if (!instructorId.equals(currentUserId)) {
            return ResponseEntity.status(403).build();
        }



//        Long instructorId = course.getInstructor().getUserId();

        return ResponseEntity.ok(courseService.updateCourse(courseid, course));


    }

}
