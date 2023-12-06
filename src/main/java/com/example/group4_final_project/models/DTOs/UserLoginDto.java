package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
public class UserLoginDto {

    private String email;



    private String password;
}
