package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    private  int id;
    @Pattern(regexp = "^$|^[a-zA-Z]{2,8}$", message = "First name must be between 2 and 8 characters")
    private String firstName;




    @Pattern(regexp = "^$|^[a-zA-Z]{2,8}$", message = "First name must be between 2 and 8 characters")
    private String lastName;



    @Pattern(regexp = "^$|^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8}$",
            message ="The password length must be more than 8 symbols and must be  " +
                    "should contain capital letter, digit, and special symbol.")
    private String password;

    MultipartFile image;



}
