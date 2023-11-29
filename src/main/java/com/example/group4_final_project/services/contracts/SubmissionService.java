package com.example.group4_final_project.services.contracts;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SubmissionService {

    void submitAssignment(Integer lectureId, Integer userId, MultipartFile file) throws IOException;
}
