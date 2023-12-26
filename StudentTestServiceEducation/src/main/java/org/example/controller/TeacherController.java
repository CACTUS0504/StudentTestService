package org.example.controller;

import org.example.entity.Role;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.service.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    TeacherServiceImpl teacherService;

    @GetMapping("/tests")
    public ResponseEntity getTeachers(@RequestHeader("teacher_id") String teacherId,
                                      @RequestHeader("user_id") String userId,
                                      @RequestHeader("roles") String roles){

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.ok().body(
                    teacherService.getTeacherTests(Long.valueOf(teacherId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-student")
    public ResponseEntity addStudent(@RequestHeader("teacher_id") String teacherId,
                                      @RequestHeader("user_id") String userId,
                                      @RequestHeader("roles") String roles,
                                      @RequestHeader("student_id") String studentId) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            teacherService.addStudentToTeacher(Long.valueOf(teacherId), Long.valueOf(studentId), Long.valueOf(userId), listRoles);
            return ResponseEntity.ok().body("Student added to selected teacher");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
