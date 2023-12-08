package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AssignmentHelper;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.AssignmentRepository;
import com.example.group4_final_project.repositories.SubmissionRepository;
import com.example.group4_final_project.repositories.UserRepository;
import com.example.group4_final_project.services.contracts.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AssignmentHelper assignmentHelper;

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository, UserRepository userRepository, AssignmentHelper assignmentHelper) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.assignmentHelper = assignmentHelper;
    }

    public Submission save(Submission submission) {
        return submissionRepository.save(submission);
    }

    public String handleAssignmentUpload(Integer lectureId, Integer userId, MultipartFile file) throws IOException, EntityNotFoundException {
        Assignment assignment = assignmentRepository.findByLectureId(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment for lecture", lectureId));

        String fileUrl = assignmentHelper.uploadAssignment(file);

        // Check for an existing submission
        Optional<Submission> existingSubmission = submissionRepository.findByAssignmentIdAndUserId(assignment.getId(), userId);

        Submission submission;
        if (existingSubmission.isPresent()) {
            // Update existing submission
            submission = existingSubmission.get();
            submission.setSubmissionURL(fileUrl);
            submission.setSubmittedAt(Instant.now());
        } else {
            // Create new submission
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            submission = new Submission();
            submission.setUser(user);
            submission.setAssignment(assignment);
            submission.setSubmissionURL(fileUrl);
            submission.setSubmittedAt(Instant.now());
        }

        save(submission);
        return fileUrl;
    }

}
