package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
public class UserRegisterDto {
    @NotNull
    @Length(min = 2, max = 20)
    private String firstName;

    @NotNull
    @Length(min = 2, max = 20)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 8, message = "Length must be more than 8 symbols")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "The password must be  should contain capital letter, digit, and special symbol.")

    private String password;

   /* @NotNull
    @Length(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String passwordConfirm;*/

    @NotNull
    private String roleName;

}
