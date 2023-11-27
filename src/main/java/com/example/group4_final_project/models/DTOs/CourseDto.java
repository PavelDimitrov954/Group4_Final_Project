package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.models.models.Enrollment;
import com.example.group4_final_project.models.models.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

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

    private User teacher;

    private int rating;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Set<Enrollment> enrollments;
}
