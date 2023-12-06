package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.models.Submission;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SubmissionService {

    Submission save(Submission submission);
    String handleAssignmentUpload(Integer lectureId, Integer userId, MultipartFile file) throws IOException, EntityNotFoundException;

}
