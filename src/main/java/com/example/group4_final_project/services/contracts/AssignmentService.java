package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.AssignmentDto;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.User;

import java.util.List;

public interface AssignmentService {
    Assignment getById(int id);

    List<Assignment> getAllAssignments();

    AssignmentDto save(Integer lectureId, AssignmentDto assignmentDto, User user);

    public AssignmentDto update(Integer id, AssignmentDto assignmentDto, User user);
    void delete(Integer lectureId, User user);
}
