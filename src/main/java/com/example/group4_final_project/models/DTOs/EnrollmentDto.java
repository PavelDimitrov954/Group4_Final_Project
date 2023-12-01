package com.example.group4_final_project.models.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class EnrollmentDto {
    private int id;
    private Timestamp enrolledAt;
    private ResponseUser user;
}
