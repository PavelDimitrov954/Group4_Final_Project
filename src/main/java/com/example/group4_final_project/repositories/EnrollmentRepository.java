package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    Optional<Enrollment> findByStudentIdAndCourseId(int studentId, int courseId);

}
