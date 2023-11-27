package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.models.Assignment;

import java.util.List;

public interface AssignmentService {
    Assignment getById(int id);

    List<Assignment> getAllAssignments();

    Assignment save(Assignment assignmentToSave);

    Assignment update(int id, Assignment assignment);

    void delete(int id);
}
