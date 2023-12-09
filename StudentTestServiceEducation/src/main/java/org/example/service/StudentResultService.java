package org.example.service;

import org.example.entity.*;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;

import java.util.List;

public interface StudentResultService {
    void addResult(Test test, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Float completeTest(Long resultId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Float getMark(Long resultId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    void addAnswer(Test test, Long questionId, Integer answer, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;

    List<Answer> getStudentAnswers(Test test, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    //List<Student> getMarks(Long questionId, List<Role> roles) throws NoRightsException ;
    //void deleteMark(Long questionId, Long userId, List<Role> roles)
    //        throws NoRightsException, NotFoundEntityException;
}
