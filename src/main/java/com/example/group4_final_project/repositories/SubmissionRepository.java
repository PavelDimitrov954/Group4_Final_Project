package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.models.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    Optional<Submission> findAllByUser(User user);

    Optional<Submission> findByAssignmentIdAndUserId(Integer id, Integer userId);
}
