package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission,Integer> {
}
