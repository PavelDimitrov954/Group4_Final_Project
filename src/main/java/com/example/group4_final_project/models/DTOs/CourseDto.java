package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
public class CourseDto {


    private int courseId;
    @NotNull
    @Length(min = 5, max = 50)
    private String title;

    @NotNull
    private String topic;

    @Length(max = 1000)
    private String description;

    @DateTimeFormat
    private Date startDate;

    private ResponseUser teacher;

    @Nullable
    private Integer rating;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Nullable
    private Set<EnrollmentDto> enrollments;
}
