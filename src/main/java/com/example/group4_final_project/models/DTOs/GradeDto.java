package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Lecture;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class GradeDto {


    private String courseTitle;
    private Map<Lecture, Double> grades;

    double abvGrade;
}
