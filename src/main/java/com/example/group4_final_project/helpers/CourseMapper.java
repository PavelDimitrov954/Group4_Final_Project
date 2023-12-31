package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.models.DTOs.CreateCourseDto;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.repositories.CourseRepository;
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
    private final CourseRepository courseRepository;

    @Autowired
    public CourseMapper(UserMapper userMapper, EnrollmentMapper enrollmentMapper, CourseRepository courseRestController) {
        this.userMapper = userMapper;
        this.enrollmentMapper = enrollmentMapper;

        this.courseRepository = courseRestController;
    }

    public CourseDto toDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(course.getId());
        courseDto.setDescription(course.getDescription());
        courseDto.setTitle(course.getTitle());
        courseDto.setTeacher(course.getTeacher());
          return courseDto;

    }

    public CreateCourseDto toCreateDto(Course course) {
       CreateCourseDto createCourseDto = new CreateCourseDto();
       createCourseDto.setTitle(course.getTitle());
       createCourseDto.setDescription(course.getDescription());
       createCourseDto.setTopic(course.getTopic().getName());

        return createCourseDto;
    }

    public CourseDtoView toDtoView(Course course) {
        CourseDtoView dto = new CourseDtoView();
        dto.setDescription(course.getDescription());
        dto.setTopic(course.getTopic().getName());
        dto.setTitle(course.getTitle());
        dto.setRating(course.getRating());
        if (course.getEnrollments() != null) {
            dto.setEnrollments(enrollmentMapper.toDto(course.getEnrollments()));
        }
        dto.setTeacher(userMapper.fromUser(course.getTeacher()));
        dto.setStartDate(course.getStartDate());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setCourseId(course.getId());
        dto.setUpdatedAt(course.getUpdatedAt());
        return dto;
    }

    public Course toEntity(CreateCourseDto dto) {
       // null
        Course course = new Course();
        course.setDescription(dto.getDescription());
        // course.setTopic(dto.getTopic());
        course.setTitle(dto.getTitle());
        course.setUpdatedAt(Timestamp.from(Instant.now()));
        course.setCreatedAt(Timestamp.from(Instant.now()));
        course.setStatus("Active");
        return course;
    }

    public List<CourseDtoView> toDtoViews(List<Course> courses) {
        return new ArrayList<>(courses.stream().map(this::toDtoView)
                .toList());
    }

    public List<CourseDto> toDto(List<Course> courses) {
        return new ArrayList<>(courses.stream().map(this::toDto)
                .toList());
    }

    public List<Course> toEntity(List<CreateCourseDto> dtos) {
        return new ArrayList<>(dtos.stream().map(this::toEntity)
                .toList());
    }

    public Course fromDto(CourseDto coursedto) {
       return courseRepository.findById(coursedto.getCourseId()).get();

    }
}
