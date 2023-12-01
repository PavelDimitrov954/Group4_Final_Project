package com.example.group4_final_project.models.DTOs;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ResponseUserTeacher extends ResponseUser {
    List<CourseDto> course;
}
