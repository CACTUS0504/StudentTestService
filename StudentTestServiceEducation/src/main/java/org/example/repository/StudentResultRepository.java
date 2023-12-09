package org.example.repository;

import org.example.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {
}