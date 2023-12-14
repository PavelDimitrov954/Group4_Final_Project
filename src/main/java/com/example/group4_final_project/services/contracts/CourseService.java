package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.User;

import java.util.List;

public interface CourseService {
    List<CourseDtoView> getAllCourses(FilterOptionsCourse filterOptionsCourse);
    List<CourseDto> getAllCoursesBYUser(User user);

    CourseDtoView getById(int id);

    List<CourseDtoView> getAllCourses();

    CourseDtoView createCourse(User userWhoCreates, CreateCourseDto courseDto);

    CourseDtoView updateCourse(User userWhoUpdates, int courseId, UpdateCourseDto courseDto);

    void delete(User userWhoDeletes, int id);

    List<CourseTopicDto> getAllCourseTopics();

    List<CourseDtoView> getTopCoursesByRating(int limit);
}
