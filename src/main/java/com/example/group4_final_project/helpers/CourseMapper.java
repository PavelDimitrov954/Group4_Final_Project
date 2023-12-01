package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.DTOs.CreateCourseDto;
import com.example.group4_final_project.models.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {
    private final UserMapper userMapper;
    private final EnrollmentMapper enrollmentMapper;

    @Autowired
    public CourseMapper(UserMapper userMapper, EnrollmentMapper enrollmentMapper) {
        this.userMapper = userMapper;
        this.enrollmentMapper = enrollmentMapper;
    }

    public CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setDescription(course.getDescription());
        dto.setTopic(course.getTopic().getName());
        dto.setTitle(course.getTitle());
        dto.setRating(course.getRating());
        dto.setEnrollments(enrollmentMapper.toDto(course.getEnrollments()));
        dto.setTeacher(userMapper.fromUser(course.getTeacher()));
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

    public List<CourseDto> toDto(List<Course> courses) {
        return new ArrayList<>(courses.stream().map(this::toDto)
                .toList());
    }

    public List<Course> toEntity(List<CreateCourseDto> dtos) {
        return new ArrayList<>(dtos.stream().map(this::toEntity)
                .toList());
    }
}
