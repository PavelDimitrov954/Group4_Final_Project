package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.Lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer>, JpaSpecificationExecutor<Lecture> {
    // Find lectures by course
    List<Lecture> findByCourseId(Integer courseId);

    // Find lectures by title containing a string
    List<Lecture> findByTitleContaining(String title);
}
