package com.example.group4_final_project.models.Lecture;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LectureDto {
    private String title;
    private String description;
    private String videoLink;
}
