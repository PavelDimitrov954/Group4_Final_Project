package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.repositories.AssignmentRepository;
import com.example.group4_final_project.repositories.LectureRepository;
import com.example.group4_final_project.services.contracts.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LectureRepository lectureRepository;
    private final String fileStorageLocation;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, LectureRepository lectureRepository, String fileStorageLocation) {
        this.assignmentRepository = assignmentRepository;
        this.lectureRepository = lectureRepository;
        this.fileStorageLocation = fileStorageLocation;
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
            throw new EntityNotFoundException("Course", id);
        }
    }

    public void saveAssignmentFile(Integer lectureId, MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(fileStorageLocation).resolve(fileName);

        // Save the file
        Files.copy(file.getInputStream(), filePath);

        // Link to lecture and save in database
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", lectureId));

        Assignment assignment = new Assignment();
        assignment.setLecture(lecture); // Fetch or reference the Lecture entity
        assignment.setFilePath(filePath.toString()); // Set the file path
        assignment.setTitle("Some title"); // Set other properties as required

        assignmentRepository.save(assignment);
    }
}
