package com.example.group4_final_project.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDto {
    @NotNull
    @Length(min = 2,max = 20)
    private String firstName;

    @NotNull
    @Length(min = 2,max = 20)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 8)
    private String password;

    @NotNull
    private String roleName;

}
