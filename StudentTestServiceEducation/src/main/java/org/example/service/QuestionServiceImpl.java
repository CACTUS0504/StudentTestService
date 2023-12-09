package org.example.service;

import org.example.entity.*;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    @Lazy
    TestServiceImpl testService;
    @Override
    public void addQuestion(Question question, Long testId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Test test = testService.getTest(testId, userId, roles);

            if (test.getOwnerId().equals(userId)) {
                question.setTest(test);
                questionRepository.save(question);
            }
        } else {
            throw new NoRightsException("User doesn't have rights to add questions");
        }
    }

    @Override
    public void updateQuestion(Question question, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {

    }

    @Override
    public Question getQuestion(Long questionId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        return null;
    }

    @Override
    public List<Student> getQuestions(Long questionId, List<Role> roles) throws NoRightsException {
        return null;
    }

    @Override
    public void deleteQuestion(Long questionId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {

    }
}
