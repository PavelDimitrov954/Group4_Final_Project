package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SubmissionDto {

    private Lecture lecture;
    List<Submission> submission;
}
