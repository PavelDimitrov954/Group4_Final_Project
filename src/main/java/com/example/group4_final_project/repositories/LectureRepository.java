package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer>, JpaSpecificationExecutor<Lecture> {
    // Find lectures by course
    Optional<List<Lecture>> findByCourseId(Integer courseId);

    // Find lectures by title containing a string
    Optional<List<Lecture>> findByTitleContaining(String title);
}
