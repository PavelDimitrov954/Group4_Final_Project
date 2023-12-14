package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.SubmissionUpdateDto;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.repositories.SubmissionRepository;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    private final SubmissionRepository submissionRepository;

    public SubmissionMapper(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission fromDto(int submissionId, SubmissionUpdateDto submissionUpdateDto) {

        Submission submission = submissionRepository.getReferenceById(submissionId);
        System.out.println(submissionUpdateDto.getGrade());
        submission.setGrade(submissionUpdateDto.getGrade());

        return submission;
    }
}
