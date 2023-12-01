package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.AssignmentDto;
import com.example.group4_final_project.models.models.Assignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public AssignmentDto toDto(Assignment assignment) {

        AssignmentDto dto = new AssignmentDto();
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        return dto;
    }

    public Assignment toEntity(AssignmentDto dto) {

        Assignment assignment = new Assignment();
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        return assignment;
    }
}
