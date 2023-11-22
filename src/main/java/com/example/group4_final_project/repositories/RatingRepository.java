package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    // Find ratings for a specific course
    List<Rating> findByCourseId(Integer courseId);

    // Find ratings by student
    List<Rating> findByStudentId(Integer studentId);

     @Query("SELECT AVG(score) FROM Rating WHERE course.id = :courseId")
     Double findAverageScoreByCourseId(Integer courseId);
}
