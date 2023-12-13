package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.User;
import lombok.Data;

import java.util.List;


@Data
public class CourseDto {


    private int courseId;
    private String title;
    private String description;
    private User teacher;
    private List<Lecture> lectures;




}
