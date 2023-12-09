package org.example.service;

import org.example.entity.Role;
import org.example.entity.Teacher;
import org.example.entity.Test;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;

import java.util.List;

public interface TeacherService {
    void addTeacher(Teacher teacher, Long userId, List<Role> roles) throws NoRightsException;
    Teacher getTeacher(Long teacherId, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException;
    List<Test> getTeacherTests(Long teacherId, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException;
    void updateTeacher(Teacher teacher, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException;
    void deleteTeacher(Long teacherId, Long userId, List<Role> roles) throws
            NoRightsException, NotFoundEntityException;
}
