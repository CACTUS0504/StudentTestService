package org.example.controller;

import org.example.entity.Role;
import org.example.entity.StudentResult;
import org.example.entity.Test;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.service.StudentResultServiceImpl;
import org.example.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    TestServiceImpl testService;
    @Autowired
    StudentResultServiceImpl studentResultService;

    @GetMapping()
    public ResponseEntity getTest(@RequestHeader("test_id") String testId,
                                     @RequestHeader("user_id") String userId,
                                     @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.status(HttpStatus.OK).body(testService.getTest(
                    Long.valueOf(testId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping()
    public ResponseEntity updateTest(@RequestHeader("user_id") String userId,
                                        @RequestHeader("roles") String roles,
                                        @RequestBody() Test test) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            testService.updateTest(test, Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student successfully updated");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    public ResponseEntity deleteTest(@RequestHeader("test_id") String testId,
                                        @RequestHeader("user_id") String userId,
                                        @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            testService.deleteTest(Long.valueOf(testId), Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Test successfully deleted");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity addTest(@RequestHeader("user_id") String userId,
                                     @RequestHeader("teacher_id") String teacherId,
                                     @RequestHeader("roles") String roles,
                                     @RequestBody() Test test) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();
        try {
            testService.addTest(test, Long.valueOf(teacherId), Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.CREATED).body("Test successfully created");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/answers")
    public ResponseEntity getAnswers(@RequestHeader("test_id") String testId,
                                  @RequestHeader("user_id") String userId,
                                  @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.status(HttpStatus.OK).body(testService.getAnswers(
                    Long.valueOf(testId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping ("/start")
    public ResponseEntity startTest(@RequestHeader("test_id") String testId,
                                     @RequestHeader("student_id") String studentId,
                                     @RequestHeader("user_id") String userId,
                                     @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            testService.startTest(
                    Long.valueOf(testId), Long.valueOf(studentId), Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.OK).body("Test started");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping ("/end")
    public ResponseEntity endTest(@RequestHeader("test_id") String testId,
                                    @RequestHeader("student_id") String studentId,
                                    @RequestHeader("user_id") String userId,
                                    @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            Float res = testService.endTest(
                    Long.valueOf(testId), Long.valueOf(studentId), Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Добавление ответа на конкретный вопрос конкретного теста
    @PostMapping ("/answer")
    public ResponseEntity postAnswer(@RequestHeader("test_id") String testId,
                                      @RequestHeader("student_id") String studentId,
                                      @RequestHeader("user_id") String userId,
                                      @RequestHeader("roles") String roles,
                                      @RequestHeader("answer") Integer answer,
                                      @RequestParam("question_id") String question_id) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            testService.addAnswer(
                    Long.valueOf(testId), Long.valueOf(question_id), Long.valueOf(studentId), answer,
                    Long.valueOf(userId), listRoles);
            return ResponseEntity.status(HttpStatus.OK).body("Answer saved");
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/student-answers")
    public ResponseEntity getStudentAnswers(@RequestHeader("test_id") String testId,
                                            @RequestHeader("user_id") String userId,
                                            @RequestHeader("student_id") String studentId,
                                            @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.status(HttpStatus.OK).body(testService.getStudentAnswers(
                    Long.valueOf(testId), Long.valueOf(studentId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/result")
    public ResponseEntity getResult(@RequestHeader("test_id") String testId,
                                            @RequestHeader("user_id") String userId,
                                            @RequestHeader("student_id") String studentId,
                                            @RequestHeader("roles") String roles) {

        List<Role> listRoles = Arrays.stream(roles.substring(1, roles.length() - 1)
                        .split(", "))
                .map(Role::valueOf)
                .toList();

        try {
            return ResponseEntity.status(HttpStatus.OK).body(testService.getStudentResult(
                    Long.valueOf(testId), Long.valueOf(studentId), Long.valueOf(userId), listRoles));
        } catch (NoRightsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
