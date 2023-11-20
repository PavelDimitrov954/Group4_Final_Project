package com.example.group4_final_project.services;

import com.example.group4_final_project.models.Course;

import java.util.List;

public interface CourseService {
    Course getById(int id);

    List<Course> getAllCourses();
    Course createCourse(Course course);
    Course update(int id, Course courseUpdate) ;
    void delete(int id);
}
