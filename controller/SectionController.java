package com.hashedin.huspark.controller;

import com.hashedin.huspark.model.Course;
import com.hashedin.huspark.model.Section;
import com.hashedin.huspark.model.Subsection;
import com.hashedin.huspark.model.Users;
import com.hashedin.huspark.repository.CourseRepository;
import com.hashedin.huspark.repository.SectionRepository;
import com.hashedin.huspark.repository.UserRepository;
import com.hashedin.huspark.service.CourseService;
import com.hashedin.huspark.service.JWTService;
import com.hashedin.huspark.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instructor")
public class SectionController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping("/course/{courseid}/create-section")
    public ResponseEntity<Course> createSection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                @PathVariable Long courseid,
                                                @RequestBody Section section) {
        // Remove "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long instructorId = userRepository.findByUsername(username).getUserid();

        return ResponseEntity.ok(sectionService.createSection(instructorId, courseid, section));

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping("/course/{courseid}/section/{sectionid}/create-subsection")
    public ResponseEntity<Course> createSubsection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                   @PathVariable Long courseid,
                                                   @PathVariable Long sectionid,
                                                   @RequestBody Subsection subsection) {
        // Remove "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long instructorId = userRepository.findByUsername(username).getUserid();

        return ResponseEntity.ok(sectionService.createSubsection(instructorId, courseid, sectionid, subsection));

    }

//    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
//    @GetMapping("/all-courses")
//    public ResponseEntity<List<Course>> getAllCourses() {
//
//        return ResponseEntity.ok(courseService.getAllCourses());
//
//    }
//
//    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
//    @GetMapping("/{id}")
//    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
//
//        return ResponseEntity.ok(courseService.getCourseById(id));
//
//    }
//
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/course/{courseid}/section/{sectionid}")
    public ResponseEntity<String> deleteCourse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long courseid,
                                               @PathVariable Long sectionid) {
        System.out.println("Hello");
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        Long currentUserId = userRepository.findByUsername(username).getUserid();

        Optional<Course> course = Optional.ofNullable(courseRepository.findById(courseid)
                .orElseThrow(() -> new RuntimeException("Course Not Found")));

        Long instructorId = course
                .map(Course::getInstructor)
                .map(Users::getUserid)
                .orElse(null);

        if (!instructorId.equals(currentUserId)) {
            return ResponseEntity.badRequest().body("You are not authorized to perform this action.");
        }



//        Long instructorId = course.getInstructor().getUserId();

        return ResponseEntity.ok(sectionService.deleteSection(courseid, sectionid));


    }
//
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PatchMapping("/course/{courseid}/section/{sectionid}")
    public ResponseEntity<Section> updateSection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathVariable Long courseid,
                                               @PathVariable Long sectionid,
                                               @RequestBody Section section) {
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

        return ResponseEntity.ok(sectionService.updateSection(courseid, sectionid, section));


    }

}
