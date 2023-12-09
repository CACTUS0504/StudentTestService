package org.example.service;

import org.example.entity.Question;
import org.example.entity.Role;
import org.example.entity.Student;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;

import java.util.List;

public interface QuestionService {
    void addQuestion(Question question, Long testId, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException;
    void updateQuestion(Question question, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Question getQuestion(Long questionId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    List<Student> getQuestions(Long questionId, List<Role> roles) throws NoRightsException ;
    void deleteQuestion(Long questionId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
}
