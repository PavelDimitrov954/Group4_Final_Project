package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.DTOs.CreateCourseDto;
import com.example.group4_final_project.models.DTOs.UpdateCourseDto;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.User;

import java.util.List;

public interface CourseService {
    List<CourseDto> getAllCourses(FilterOptionsCourse filterOptionsCourse);

    CourseDto getById(int id);

    List<CourseDto> getAllCourses();

    CourseDto createCourse(User userWhoCreates, CreateCourseDto courseDto);

    CourseDto updateCourse(User userWhoUpdates, int courseId, UpdateCourseDto courseDto);

    void delete(User userWhoDeletes, int id);
}
