package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.exceptions.UnauthorizedOperationException;
import com.example.group4_final_project.models.DTOs.SubmissionDto;
import com.example.group4_final_project.models.DTOs.SubmissionUpdateDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.models.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface SubmissionService {

    Submission save(Submission submission);
    String handleAssignmentUpload(Integer lectureId, Integer userId, MultipartFile file) throws IOException, EntityNotFoundException;

    void update(int id, User user, SubmissionUpdateDto submissionUpdateDto);


    List<SubmissionDto> getStudentSubmission(User loginUser);

}
