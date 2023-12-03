package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AssignmentHelper;
import com.example.group4_final_project.helpers.AssignmentMapper;
import com.example.group4_final_project.helpers.LectureMapper;
import com.example.group4_final_project.models.DTOs.AssignmentDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.models.models.User;
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
    private final AssignmentMapper assignmentMapper;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, LectureRepository lectureRepository, AssignmentHelper assignmentHelper, AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.lectureRepository = lectureRepository;
        this.assignmentHelper = assignmentHelper;
        this.assignmentMapper = assignmentMapper;
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
    public AssignmentDto save(Integer lectureId, AssignmentDto assignmentDto, User user) {
        // Check if the lecture exists
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", lectureId));

        // Authorization check: Ensure the user is a teacher/admin
        if (!isUserAuthorizedToCreateOrModifyAssignment(user, lecture)) {
            throw new AuthorizationException("User not authorized to create assignments for this lecture.");
        }

        // Convert DTO to Entity and Set Lecture
        Assignment assignment = assignmentMapper.toEntity(assignmentDto);
        assignment.setLecture(lecture);

        // Save the Assignment
        Assignment savedAssignment = assignmentRepository.save(assignment);

        return assignmentMapper.toDto(savedAssignment);
    }

    @Override
    @Transactional
    public AssignmentDto update(Integer id, AssignmentDto assignmentDto, User user) {
        // Fetch the existing lecture
        Lecture existingLecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", id));

        // Authorization check
        if (!isUserAuthorizedToCreateOrModifyAssignment(user, existingLecture)) {
            throw new AuthorizationException("User not authorized to update this assignment.");
        }

        // Update fields from DTO
        Assignment assignment = existingLecture.getAssignment();
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());

        // Save the updated assignment
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Return the updated DTO
        return assignmentMapper.toDto(savedAssignment);
    }

    @Override
    @Transactional
    public void delete(Integer lectureId, User user) {
        // Fetch the existing lecture
        Lecture existingLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", lectureId));

        // Authorization check
        if (!isUserAuthorizedToCreateOrModifyAssignment(user, existingLecture)) {
            throw new AuthorizationException("User not authorized to delete this assignment.");
        }

        Assignment assignment = assignmentRepository.findByLectureId(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment", lectureId));
        if (assignment != null) {
            existingLecture.setAssignment(null); // Disassociate the Assignment
            lectureRepository.save(existingLecture); // Save the Lecture to remove the association
            assignmentRepository.delete(assignment); // Delete the Assignment
        } else {
            throw new EntityNotFoundException("Assignment for lecture", lectureId); // Assignment not found
        }
    }

    private boolean isUserAuthorizedToCreateOrModifyAssignment(User user, Lecture lecture) {

        // Check if the user is the teacher of the course
        if (user.equals(lecture.getCourse().getTeacher())) {
            return true;
        }

        // Check if the user has the ADMIN role
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == RoleName.ADMIN) {
                return true;
            }
        }

        // User is not authorized
        return false;
    }
}
