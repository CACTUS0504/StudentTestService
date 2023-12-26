package org.example.service;

import org.example.entity.Role;
import org.example.entity.Student;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    StudentRepository studentRepository;

    @Override
    public void addStudent(Student student, Long userId, List<Role> roles) throws NoRightsException {
        if (roles.contains(Role.STUDENT)) {
            student.setOwnerId(userId);
            studentRepository.save(student);
        } else {
            throw new NoRightsException("User doesn't have rights to add students");
        }
    }

    @Override
    public void updateStudent(Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Optional<Student> oldStudent = studentRepository.findById(student.getId());

            if (oldStudent.isPresent()) {
                if (oldStudent.get().getOwnerId().equals(userId)) {
                    student.setOwnerId(userId);
                    studentRepository.save(student);
                }
            } else {
                throw new NotFoundEntityException("Required student doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to add students");
        }
    }

    @Override
    public Student getStudent(Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Optional<Student> student = studentRepository.findById(studentId);

            if (student.isPresent()) {
                if (Objects.equals(userId, student.get().getOwnerId())) {
                    return student.get();
                } else {
                    throw new NoRightsException("user doesn't have right to get this student");
                }
            } else {
                throw new NotFoundEntityException("Required student doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to get students");
        }
    }

    @Override
    public Student getCurrentStudent(Long userId, List<Role> roles) throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Student student = studentRepository.findAllByOwnerId(userId).get(0);
                if (Objects.equals(userId, student.getOwnerId())) {
                    return student;
                } else {
                    throw new NoRightsException("user doesn't have right to get this student");
                }
            } else {
                throw new NotFoundEntityException("Required student doesn't exist");
            }
    }

    @Override
    public List<Student> getStudents(Long userId, List<Role> roles) throws NoRightsException {
        if (roles.contains(Role.ADMIN)) {
            List<Student> students = studentRepository.findAll();
        }
        throw new NoRightsException("User doesn't have rights to get all students");
    }

    @Override
    public void deleteStudent(Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.STUDENT)) {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                if (Objects.equals(userId, student.get().getOwnerId())) {
                    studentRepository.deleteById(studentId);
                }
            } else {
                throw new NotFoundEntityException("Required student doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to delete students");
        }
    }
}
