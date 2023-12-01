package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.models.Course;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class CourseMapper {
    public CourseDtoView toDtoView(Course course) {
        CourseDtoView dto = new CourseDtoView();
        dto.setDescription(course.getDescription());
        dto.setTopic(course.getTopic().getName());
        dto.setTitle(course.getTitle());
        dto.setRating(course.getRating());
        dto.setEnrollments(new HashSet<>(course.getEnrollments()));
        dto.setTeacher(course.getTeacher());
        dto.setStartDate(course.getStartDate());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setCourseId(course.getId());
        dto.setUpdatedAt(course.getUpdatedAt());
        return dto;
    }

    public CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setDescription(course.getDescription());
        dto.setTopic(course.getTopic().getName());
        dto.setTitle(course.getTitle());

        return dto;
    }

    public Course toEntity(CourseDto dto) {
        Course course = new Course();
        course.setDescription(dto.getDescription());
       // course.setTopic(dto.getTopic());
        course.setTitle(dto.getTitle());
        course.setUpdatedAt(Timestamp.from(Instant.now()));
        course.setCreatedAt(Timestamp.from(Instant.now()));
        course.setStatus("Active");
        return course;
    }

    public List<CourseDtoView> toDto(List<Course> courses){
        return new ArrayList<>(courses.stream().map(this::toDtoView)
                .toList());
    }
    public List<Course> toEntity(List<CourseDto> dtos){
        return new ArrayList<>(dtos.stream().map(this::toEntity)
                .toList());
    }
}
