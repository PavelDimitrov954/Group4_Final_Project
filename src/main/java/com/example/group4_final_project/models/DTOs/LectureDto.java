package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDto {
    private Integer courseId;
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
    private String description;
    private String videoLink;
}
