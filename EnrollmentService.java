package com.hashedin.huspark.service;

import com.hashedin.huspark.model.Course;
import com.hashedin.huspark.model.CourseProgress;
import com.hashedin.huspark.model.Users;
import com.hashedin.huspark.repository.CourseProgressRepository;
import com.hashedin.huspark.repository.CourseRepository;
import com.hashedin.huspark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EnrollmentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CourseProgressRepository courseProgressRepository;



    public ResponseEntity<Users> enrollStudent(String token, Long courseid) {

        String username = jwtService.extractUsername(token);

        Long userid = userRepository.findByUsername(username).getUserid();
        Optional<Users> studentOpt = userRepository.findById(userid);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Course> courseOpt = courseRepository.findById(courseid);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Users student = studentOpt.get();
        Course course = courseOpt.get();

        List<Course> courses = student.getCourses();
        courses.add(course);
//        System.out.println(courses);
        student.setCourses(courses);

        Users updatedStudent = userRepository.save(student);

        course.getEnrolledStudents().add(updatedStudent);
        courseRepository.save(course);

        return ResponseEntity.ok(updatedStudent);
    }

    public ResponseEntity<String> markComplete(String token, Long courseid, Long subsectionid) {

        String username = jwtService.extractUsername(token);

        Long userid = userRepository.findByUsername(username).getUserid();
        Optional<Users> studentOpt = userRepository.findById(userid);
        Users student = studentOpt.get();

        Optional<Course> courseOpt = courseRepository.findById(courseid);
        Course course = courseOpt.get();

        boolean isEnrolled = course.getEnrolledStudents().contains(student);

        if(!isEnrolled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not enrolled in this course");
        }

        CourseProgress progress = new CourseProgress();
        progress.setCourseid(courseid);
        progress.setUserid(userid);
        progress.setSubsectionId(subsectionid);

        courseProgressRepository.save(progress);

        return ResponseEntity.ok("Marked Successfully");


    }


    public ResponseEntity<List<Course>> getAllCoursesById(String token) {
        String username = jwtService.extractUsername(token);

        Long userid = userRepository.findByUsername(username).getUserid();
        Optional<Users> studentOpt = userRepository.findById(userid);
        Users student = studentOpt.get();

        List<Course> allCourses = student.getCourses();

        return ResponseEntity.ok(allCourses);

    }




}
