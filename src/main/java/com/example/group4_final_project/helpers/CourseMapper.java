package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.DTOs.CreateCourseDto;
import com.example.group4_final_project.models.DTOs.UpdateCourseDto;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Enrollment;
import com.example.group4_final_project.repositories.CourseTopicRepository;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CourseMapper {
    public CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
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

    public Course toEntity(CreateCourseDto dto) {
        Course course = new Course();
        course.setDescription(dto.getDescription());
       // course.setTopic(dto.getTopic());
        course.setTitle(dto.getTitle());
        course.setUpdatedAt(Timestamp.from(Instant.now()));
        course.setCreatedAt(Timestamp.from(Instant.now()));
        course.setStatus("Active");
        return course;
    }

    public List<CourseDto> toDto(List<Course> courses){
        return new ArrayList<>(courses.stream().map(this::toDto)
                .toList());
    }
    public List<Course> toEntity(List<CreateCourseDto> dtos){
        return new ArrayList<>(dtos.stream().map(this::toEntity)
                .toList());
    }
}
