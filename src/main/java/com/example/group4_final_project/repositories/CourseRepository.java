package com.example.group4_final_project.repositories;


import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {

    Optional<Course> findAllByTeacher(User user);


}
