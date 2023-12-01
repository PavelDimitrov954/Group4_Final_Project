package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CourseDto {
    @NotNull
    @Length(min = 5, max = 50)
    private String title;

    @NotNull
    private String topic;

    @Length(max = 1000)
    private String description;

}
