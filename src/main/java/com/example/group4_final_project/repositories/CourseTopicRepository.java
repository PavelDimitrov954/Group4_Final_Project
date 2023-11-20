package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.CourseTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTopicRepository extends JpaRepository<CourseTopic, Integer> {
}
