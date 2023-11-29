package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    Optional<Assignment> findByLectureId(Integer lectureId);
}
