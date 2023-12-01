package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class LectureDto {
    private Integer courseId;
    @NotNull
    @Size(min = 5, max = 50)
    private String title;
    @Size(max = 1000)
    private String description;

    private AssignmentDto assignment;

    @URL
    private String videoLink;
}
