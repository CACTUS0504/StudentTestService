package org.example.service;

import org.example.entity.*;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.repository.StudentResultRepository;
import org.example.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestRepository testRepository;
    @Autowired
    TeacherServiceImpl teacherService;

    @Autowired
    StudentResultServiceImpl studentResultService;
    @Autowired
    StudentServiceImpl studentService;
    @Override
    public void addTest(Test test, Long teacherId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Teacher teacher = teacherService.getTeacher(teacherId, userId, roles);

            if (teacher.getOwnerId().equals(userId)) {
                test.setOwnerId(userId);
                test.setTeacher(teacher);

                // Если пихнуть просто test, id теста не добавится в вопросы
                // Подумать над правильным маппингом
                List<Question> questions = test.getQuestions();
                for (Question question : questions) {
                    question.setTest(test);
                }
                test.setQuestions(questions);
                testRepository.save(test);
            }
        } else {
            throw new NoRightsException("User doesn't have rights to add tests");
        }
    }

    @Override
    public Test getTest(Long testId, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException {
        Optional<Test> test = testRepository.findById(testId);

        if (roles.contains(Role.TEACHER)) {
            if (test.isPresent()) {
                if (Objects.equals(userId, test.get().getOwnerId())) {
                    return test.get();
                } else {
                    throw new NoRightsException("User doesn't have rights to get this test");
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        }
        if (roles.contains(Role.STUDENT)) {
            if (test.isPresent()) {
                if (test.get().getTeacher().getStudents().stream()
                        .anyMatch(student -> student.getOwnerId().equals(userId))) {
                    return test.get();
                } else {
                    throw new NoRightsException("User doesn't have rights to get this test");
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        }
        throw new NoRightsException("User doesn't have rights to get tests");
    }

    @Override
    public void updateTest(Test test, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Optional<Test> oldTestOpt = testRepository.findById(test.getId());

            if (oldTestOpt.isPresent()) {
                Test oldTest = oldTestOpt.get();
                if (oldTest.getOwnerId().equals(userId)) {
                    oldTest.setTitle(test.getTitle());

                    List<Question> questions = test.getQuestions();
                    for (Question question : questions) {
                        question.setTest(oldTest);
                    }
                    oldTest.setQuestions(questions);
                    testRepository.save(oldTest);
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to add tests");
        }
    }

    @Override
    public void deleteTest(Long testId, Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Optional<Test> test = testRepository.findById(testId);

            if (test.isPresent()) {
                if (Objects.equals(userId, test.get().getOwnerId())) {
                    testRepository.deleteById(testId);
                } else {
                    throw new NoRightsException("User doesn't have rights to delete this test");
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to delete this test");
        }
    }

    public List<Integer> getAnswers(Long testId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Test> test = testRepository.findById(testId);

        if (roles.contains(Role.TEACHER)) {
            if (test.isPresent()) {
                if (Objects.equals(userId, test.get().getOwnerId())) {
                    return test.get().getQuestions().stream().map(Question::getAnswer).toList();
                } else {
                    throw new NoRightsException("User doesn't have rights to get this test answers");
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to get answers");
        }
    }

    @Override
    public List<Answer> getStudentAnswers(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        return studentResultService.getStudentAnswers(testRepository.getById(testId),
                studentService.getStudent(studentId, userId, roles), userId, roles);
    }

    @Override
    public Float getStudentResult(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (roles.contains(Role.STUDENT)) {
            if (testOpt.isPresent()) {
                Test test = testOpt.get();
                if (test.getTeacher().getStudents().stream().map(Student::getOwnerId).toList().contains(userId)) {
                    Student student = studentService.getStudent(studentId, userId, roles);
                    List<StudentResult> studentResults = student.getStudentResults();
                    if (studentResults == null) {
                        throw new NotFoundEntityException("Required student doesn't completing tests right now");
                    }
                    StudentResult currentTestResult = null;
                    for (StudentResult studentResult : studentResults) {
                        if (studentResult.getCompletedTest().getId().equals(testId)) {
                            currentTestResult = studentResult;
                            break;
                        }
                    }
                    if (currentTestResult == null) {
                        throw new NotFoundEntityException("User isn't completing this test right now");
                    }
                    if (!currentTestResult.getCompleted()) {
                        throw new NoRightsException("Test is not completed");
                    }
                    return studentResultService.getMark(currentTestResult.getId(), userId, roles);
                } else {
                    throw new NoRightsException("User doesn't have rights to complete this test");
                }
            } else {
                throw new NotFoundEntityException("Required tests doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to complete tests");
        }
    }

    // Изменить
    @Override
    public void addAnswer(Long testId, Long question_id, Long studentId, Integer answer, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Test> testOpt = testRepository.findById(testId);

        if (roles.contains(Role.STUDENT)) {
            if (testOpt.isPresent()) {
                Test test = testOpt.get();
                if (test.getTeacher().getStudents().stream().map(Student::getOwnerId).toList().contains(userId)) {
//                    List<Integer> answers = test.getQuestions().stream().map(Question::getAnswer).toList();
//
//                    int points = 0;
//                    if (studentAnswers.size() > answers.size()) {
//                        throw new NotFoundEntityException("too many answers");
//                    }
//                    for (int i = 0; i < Integer.min(studentAnswers.size(), answers.size()); i++) {
//                        if (Objects.equals(studentAnswers.get(i), answers.get(i))) {
//                            points++;
//                        }
//                    }
//                    Float mark;
//                    if (points == 0) {
//                        mark = 0f;
//                    } else {
//                        mark = (float)points / Integer.min(studentAnswers.size(), answers.size());
//                    }

//                    if (!test.getStudentResults().stream().map(mark1 -> mark1.getStudent().getId()).toList().contains(studentId))
//                    {
                        studentResultService.addAnswer(test, question_id, answer,
                                studentService.getStudent(studentId, userId, roles), userId, roles);
                        return;
//                    } else {
//                        throw new NoRightsException("Test already completed");
//                    }
                } else {
                    throw new NoRightsException("User doesn't have rights to complete this test");
                }
            } else {
                throw new NotFoundEntityException("Required test doesn't exist");
            }
        } else {
            throw new NoRightsException("Only students can complete tests");
        }
    }

    @Override
    public void startTest(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Test> testOpt = testRepository.findById(testId);

        if (roles.contains(Role.STUDENT)) {
            if (testOpt.isPresent()) {
                Test test = testOpt.get();
                if (test.getTeacher().getStudents().stream().map(Student::getOwnerId).toList().contains(userId)) {
                    studentResultService.addResult(test, studentService.getStudent(studentId, userId, roles),
                            userId, roles);
                } else {
                    throw new NoRightsException("User doesn't have rights to start this test");
                }
            } else {
                throw new NotFoundEntityException("Required tests doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to start tests");
        }
    }

    @Override
    public Float endTest(Long testId, Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (roles.contains(Role.STUDENT)) {
            if (testOpt.isPresent()) {
                Test test = testOpt.get();
                if (test.getTeacher().getStudents().stream().map(Student::getOwnerId).toList().contains(userId)) {
                    Student student = studentService.getStudent(studentId, userId, roles);
                    List<StudentResult> studentResults = student.getStudentResults();
                    if (studentResults == null) {
                        throw new NotFoundEntityException("Required student doesn't completing tests right now");
                    }
                    StudentResult currentTestResult = null;
                    for (StudentResult studentResult : studentResults) {
                        if (studentResult.getCompletedTest().getId().equals(testId)) {
                            currentTestResult = studentResult;
                            break;
                        }
                    }
                    if (currentTestResult == null) {
                        throw new NotFoundEntityException("User isn't completing this test right now");
                    }
                    if (currentTestResult.getCompleted()) {
                        throw new NoRightsException("Test is already completed");
                    }
                    return studentResultService.completeTest(currentTestResult.getId(), userId, roles);
                } else {
                    throw new NoRightsException("User doesn't have rights to complete this test");
                }
            } else {
                throw new NotFoundEntityException("Required tests doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to complete tests");
        }
    }
}
