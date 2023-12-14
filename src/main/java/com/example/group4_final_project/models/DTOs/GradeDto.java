package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Lecture;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class GradeDto {


    private CourseDto course;
    private Map<Lecture, Double> grades;

    double abvGrade;
}
