package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.repositories.CourseRepository;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {
    private final CourseRepository courseRepository;
    private final AssignmentMapper assignmentMapper;

    public LectureMapper(CourseRepository courseRepository, AssignmentMapper assignmentMapper) {
        this.courseRepository = courseRepository;
        this.assignmentMapper = assignmentMapper;
    }

    public LectureDto toDto(Lecture lecture) {
        LectureDto dto = new LectureDto();
        dto.setId(lecture.getId());
        dto.setCourseId(lecture.getCourse().getId());
        dto.setTitle(lecture.getTitle());
        dto.setDescription(lecture.getDescription());
        dto.setVideoLink(lecture.getVideoLink());
        if (lecture.getAssignment() != null) {
            dto.setAssignment(assignmentMapper.toDto(lecture.getAssignment()));
        }

        return dto;
    }


    public Lecture toEntity(LectureDto dto) {
        Lecture lecture = new Lecture();
        lecture.setTitle(dto.getTitle());
        lecture.setDescription(dto.getDescription());
        lecture.setVideoLink(dto.getVideoLink());
        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course", dto.getCourseId()));
            lecture.setCourse(course);
        } else {
            throw new IllegalArgumentException("Course ID is required");
        }
        if (dto.getAssignment() != null) {
            Assignment assignment = assignmentMapper.toEntity(dto.getAssignment());
            lecture.setAssignment(assignment);
        }

        return lecture;
    }
}
