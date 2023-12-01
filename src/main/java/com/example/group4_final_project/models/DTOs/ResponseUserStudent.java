package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Submission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ResponseUserStudent extends ResponseUser {
    List<CourseDto> course;
    Set<Submission> submissionSet;

}
