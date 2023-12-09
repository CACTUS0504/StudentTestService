package org.example.service;

import org.example.entity.Answer;
import org.example.entity.Role;
import org.example.entity.Student;
import org.example.entity.Test;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;

import java.util.List;

public interface TestService {
    void addTest(Test test, Long teacherId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Test getTest(Long testId, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException;
    void updateTest(Test test, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException;
    void deleteTest(Long testId, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException;
    List<Integer> getAnswers(Long testId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    List<Answer> getStudentAnswers(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Float getStudentResult(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    void addAnswer(Long testId, Long question_id, Long studentId, Integer answer, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    void startTest(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Float endTest(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
}
