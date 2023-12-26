package org.example.service;

import org.example.entity.Role;
import org.example.entity.Student;
import org.example.exceptions.NoRightsException;
import org.example.exceptions.NotFoundEntityException;

import java.util.List;

public interface StudentService {
    void addStudent(Student student, Long userId, List<Role> roles) throws NoRightsException;
    void updateStudent(Student student, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Student getStudent(Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    Student getCurrentStudent(Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
    List<Student> getStudents(Long userId, List<Role> roles) throws NoRightsException ;
    void deleteStudent(Long studentId, Long userId, List<Role> roles)
            throws NoRightsException, NotFoundEntityException;
}
