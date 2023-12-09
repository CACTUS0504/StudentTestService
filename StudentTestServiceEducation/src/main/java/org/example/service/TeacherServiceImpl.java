package org.example.service;

import org.example.entity.Role;
import org.example.entity.Student;
import org.example.entity.Teacher;
import org.example.entity.Test;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;
import org.example.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherRepository teacherRepository;
    @Override
    public void addTeacher(Teacher teacher, Long userId, List<Role> roles) throws NoRightsException {
        if (roles.contains(Role.TEACHER)) {
            teacher.setOwnerId(userId);
            teacherRepository.save(teacher);
        } else {
            throw new NoRightsException("User doesn't have rights to add teachers");
        }
    }

    @Override
    public Teacher getTeacher(Long teacherId, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Optional<Teacher> teacher = teacherRepository.findById(teacherId);

            if (teacher.isPresent()) {
                if (Objects.equals(userId, teacher.get().getOwnerId())) {
                    return teacher.get();
                } else {
                    throw new NoRightsException("User doesn't have rights to get this teacher");
                }
            } else {
                throw new NotFoundEntityException("Required teacher doesn't exist");
            }
        }
        throw new NoRightsException("User doesn't have rights to get teachers");
    }

    @Override
    public List<Test> getTeacherTests(Long teacherId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);

        if (roles.contains(Role.TEACHER)) {
            if (teacher.isPresent()) {
                if (Objects.equals(userId, teacher.get().getOwnerId())) {
                    return teacher.get().getTests();
                } else {
                    throw new NoRightsException("User doesn't have rights to get this teacher");
                }
            } else {
                throw new NotFoundEntityException("Required teacher doesn't exist");
            }
        } else if (roles.contains(Role.STUDENT)) {
            if (teacher.isPresent()) {
                if ((teacher.get().getStudents().stream().map(Student::getOwnerId)
                        .toList().contains(userId))) {
                    return teacher.get().getTests();
                } else {
                    throw new NoRightsException("Student doesn't learning from this teacher");
                }
            } else {
                throw new NotFoundEntityException("Required teacher doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to get teachers");
        }
    }

    @Override
    public void updateTeacher(Teacher teacher, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Optional<Teacher> oldTeacher = teacherRepository.findById(teacher.getId());

            if (oldTeacher.isPresent()) {
                if (oldTeacher.get().getOwnerId().equals(userId)) {
                    teacher.setOwnerId(userId);
                    teacherRepository.save(teacher);
                }
            } else {
                throw new NotFoundEntityException("Required teacher doesn't exist");
            }
        } else {
            throw new NoRightsException("User doesn't have rights to add teachers");
        }
    }

    @Override
    public void deleteTeacher(Long teacherId, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException {
        if (roles.contains(Role.TEACHER)) {
            Optional<Teacher> teacher = teacherRepository.findById(teacherId);
            if (teacher.isPresent()) {
                if (Objects.equals(userId, teacher.get().getOwnerId())) {
                    teacherRepository.deleteById(teacherId);
                    return;
                }
            } else {
                throw new NotFoundEntityException("Required teacher doesn't exist");
            }
        }
        throw new NoRightsException("User doesn't have rights to delete teachers");
    }
}
