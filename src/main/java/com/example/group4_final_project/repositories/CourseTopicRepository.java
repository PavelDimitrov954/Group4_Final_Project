package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.CourseTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseTopicRepository extends JpaRepository<CourseTopic, Integer> {

    Optional<CourseTopic> findByName(String name);
}
