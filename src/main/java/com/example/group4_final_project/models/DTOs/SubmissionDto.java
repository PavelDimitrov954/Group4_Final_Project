package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SubmissionDto {
    private Course course;
    List<Submission> submission;
}
