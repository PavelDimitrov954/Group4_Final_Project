package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.DTOs.CourseTopicDto;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.repositories.CourseTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseTopicMapper {
    private final CourseTopicRepository courseTopicRepository;

    @Autowired
    public CourseTopicMapper(CourseTopicRepository courseTopicRepository) {
        this.courseTopicRepository = courseTopicRepository;
    }

    public CourseTopicDto toDto(CourseTopic courseTopic) {
        CourseTopicDto dto = new CourseTopicDto();
        dto.setTopicName(courseTopic.getName());
        return dto;
    }

    public List<CourseTopicDto> toDto(List<CourseTopic> courseTopics) {
        return new ArrayList<>(courseTopics.stream().map(this::toDto)
                .toList());
    }

    public CourseTopic toEntity(CourseTopicDto dto){
        return courseTopicRepository.findByName(dto.getTopicName())
                .orElseThrow( () -> new EntityNotFoundException("Topic", "name", dto.getTopicName()));
    }

}
