package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.repositories.AssignmentRepository;
import com.example.group4_final_project.repositories.SubmissionRepository;
import com.example.group4_final_project.repositories.UserRepository;
import com.example.group4_final_project.services.contracts.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;  // Directory for file storage

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;

    }

    public void submitAssignment(Integer lectureId, Integer userId, MultipartFile file) throws IOException {
        // Find the assignment related to the lecture
        Assignment assignment = assignmentRepository.findByLectureId(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment for lecture", lectureId));

        // Save the file and get the file path or identifier
        String filePath = saveFile(file);

        // Create and save the Submission entity
        Submission submission = new Submission();
        submission.setUser(userRepository.findById(userId).orElseThrow());
        submission.setAssignmentSet(Set.of(assignment));
        submission.setSubmissionFilePath(filePath);
        submission.setSubmittedAt(Instant.now());

        submissionRepository.save(submission);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot save an empty file.");
        }

        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path targetLocation = Paths.get(fileStorageLocation).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return targetLocation.toString();
    }
}
