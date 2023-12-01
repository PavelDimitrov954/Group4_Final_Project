package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AssignmentHelper;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.repositories.AssignmentRepository;
import com.example.group4_final_project.repositories.LectureRepository;
import com.example.group4_final_project.services.contracts.AssignmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LectureRepository lectureRepository;
    private final AssignmentHelper assignmentHelper;


    @Value("${file.storage.location}")
    private String fileStorageLocation;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, LectureRepository lectureRepository, AssignmentHelper assignmentHelper) {
        this.assignmentRepository = assignmentRepository;
        this.lectureRepository = lectureRepository;
        this.assignmentHelper = assignmentHelper;
    }

    @Override
    public Assignment getById(int id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("id", id));
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    @Transactional
    public Assignment save(Assignment assignment) {
        if (assignmentRepository.existsById(assignment.getId())) {
            throw new EntityDuplicateException("Assignment", "id", String.valueOf(assignment.getId()));
        }
        assignmentRepository.save(assignment);
        return assignment;
    }

    @Override
    @Transactional
    public Assignment update(int id, Assignment assignment) {

        Assignment existingAssignment = getById(id);

        existingAssignment.setDescription(assignment.getDescription());
        existingAssignment.setLecture(assignment.getLecture());
        existingAssignment.setTitle(assignment.getTitle());

        return assignmentRepository.save(existingAssignment);

    }

    @Override
    @Transactional
    public void delete(int id) {
        try {
            Assignment assignmentToDelete = getById(id);
            assignmentRepository.delete(assignmentToDelete);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Assignment", id);
        }
    }
}
