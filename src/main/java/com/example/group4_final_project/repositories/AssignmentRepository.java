package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.Assignment;
import com.example.group4_final_project.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
}
