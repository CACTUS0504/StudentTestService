package org.example.controller;

import org.example.entity.Role;
import org.example.entity.Student;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    StudentServiceImpl studentService;
    @GetMapping("/teachers")
    public ResponseEntity getTeachers(@RequestHeader("student_id") String studentId,
                                      @RequestHeader("user_id") String userId,
                                      @RequestHeader("roles") String roles){

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.ok().body(
                    studentService.getStudent(Long.valueOf(studentId), Long.valueOf(userId), listRoles).getTeachers());
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity getStudent(@RequestHeader("student_id") String studentId,
                                      @RequestHeader("user_id") String userId,
                                      @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudent(
                    Long.valueOf(studentId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity deleteStudent(@RequestHeader("student_id") String studentId,
                                     @RequestHeader("user_id") String userId,
                                     @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            studentService.deleteStudent(Long.valueOf(studentId), Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Student successfully deleted");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping()
    public ResponseEntity updateStudent(@RequestHeader("user_id") String userId,
                                        @RequestHeader("roles") String roles,
                                        @RequestBody() Student student) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            studentService.updateStudent(student, Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student successfully updated");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity addStudent(@RequestHeader("user_id") String userId,
                                        @RequestHeader("roles") String roles,
                                        @RequestBody() Student student) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            studentService.addStudent(student, Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student successfully created");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
