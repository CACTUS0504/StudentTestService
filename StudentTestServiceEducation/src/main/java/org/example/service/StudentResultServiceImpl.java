package org.example.service;

import org.example.entity.*;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.repository.StudentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentResultServiceImpl implements StudentResultService {
    @Autowired
    StudentResultRepository studentResultRepository;

    @Override
    public void addResult(Test test, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            // Ищу результаты по нужному тесту
            List<StudentResult> studentResults = student.getStudentResults();

            for (StudentResult studentResult : studentResults) {
                System.out.println(studentResult.getCompletedTest().getId());
                // Если нашёл, выдаю ошибку, так как нельзя решать повторно
                if (Objects.equals(studentResult.getCompletedTest().getId(), test.getId())
                        && studentResult.getCompleted()) {
                    throw new NoRightsException("Test is already completed");
                } else if (Objects.equals(studentResult.getCompletedTest().getId(), test.getId())) {
                    throw new NoRightsException("Test is already started");
                }
            }
            // Если студент ещё не начинал данный тест, создаю сущность ответа
            studentResultRepository.save(
                    StudentResult.builder()
                            .student(student)
                            .completedTest(test)
                            .completed(false)
                            .build()
            );
        } else {
            throw new NoRightsException("User doesn't have rights to complete tests");
        }
    }

    @Override
    public Float completeTest(Long resultId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Optional<StudentResult> studentResult = studentResultRepository.findById(resultId);
            if (studentResult.isPresent()) {
                if (Objects.equals(studentResult.get().getStudent().getOwnerId(), userId)) {
                    StudentResult res = studentResult.get();
                    List<Answer> studentAnswers = res.getStudentAnswers();
                    List<Integer> correctResults = res.getCompletedTest()
                            .getQuestions().stream().map(Question::getAnswer).toList();
                    int studentCorrectAnswers = 0;
                    for (Answer answer : studentAnswers) {
                        if (answer.getAnswer().equals(correctResults.get(answer.getQuestionNum().intValue() - 1))) {
                            studentCorrectAnswers++;
                        }
                    }
                    Float mark = (float)studentCorrectAnswers / correctResults.size();
                    res.setMark(mark);
                    res.setCompleted(true);
                    studentResultRepository.save(res);
                    return mark;
                } else {
                    throw new NoRightsException("User doesn't have rights to complete this test");
                }
            } else {
                throw new NotFoundEntityException("Required student result doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to complete this test");
        }
    }

    @Override
    public Float getMark(Long resultId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Optional<StudentResult> studentResult = studentResultRepository.findById(resultId);
            if (studentResult.isPresent()) {
                if (Objects.equals(studentResult.get().getStudent().getOwnerId(), userId)) {
                    StudentResult res = studentResult.get();
                    return res.getMark();
                } else {
                    throw new NoRightsException("User doesn't have rights to get mark of this test");
                }
            } else {
                throw new NotFoundEntityException("Required student result doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to get marks");
        }
    }

    @Override
    public void addAnswer(Test test, Long questionId, Integer answer, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            // Ищу результаты по нужному тесту
            List<StudentResult> studentResults = student.getStudentResults();
            for (StudentResult studentResult : studentResults) {
                // Если нашёл, добавляю ответ на вопрос
                if (Objects.equals(studentResult.getCompletedTest().getId(), test.getId())) {
                    List<Answer> studentAnswers = studentResult.getStudentAnswers();
                    Answer currentAnswer = null;
                    for (Answer answer1: studentAnswers) {
                        if (answer1.getQuestionNum().equals(questionId)) {
                            currentAnswer = answer1;
                            break;
                        }
                    }
                    if (currentAnswer == null) {
                        currentAnswer = new Answer();
                        currentAnswer.setQuestionNum(questionId);
                        currentAnswer.setAnswer(answer);
                        currentAnswer.setStudentResult(studentResult);
                        studentAnswers.add(currentAnswer);
                    } else {
                        currentAnswer.setAnswer(answer);
                        currentAnswer.setStudentResult(studentResult);
                    }

                    studentResultRepository.save(StudentResult.builder()
                            .id(studentResult.getId())
                            .student(student)
                            .completedTest(test)
                            .studentAnswers(studentAnswers)
                            .completed(studentResult.getCompleted())
                            .build());
                    return;
                }
            }
            // Если не нашёл нужный тест, значит таймер ещё не запустился и ответ добавлять нельзя (Ответы добавляются
            // в базу после запуска таймера)
            throw new NotFoundEntityException("User isn't completing this test now");
        } else {
            throw new NoRightsException("User doesn't have rights to add answers");
        }
    }

    @Override
    public List<Answer> getStudentAnswers(Test test, Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            List<StudentResult> studentResults = student.getStudentResults();
            for (StudentResult studentResult : studentResults) {
                if (Objects.equals(studentResult.getCompletedTest().getId(), test.getId())) {
                    if (Objects.equals(studentResult.getStudent().getOwnerId(), userId)) {
                        return studentResult.getStudentAnswers();
                    } else {
                        throw new NoRightsException("User doesn't have rights to get answers of this student");
                    }
                }
            }
            throw new NotFoundEntityException("Student isn't completing this test now");
        } else {
            throw new NoRightsException("User doesn't have rights to get student's answers");
        }
    }
}
