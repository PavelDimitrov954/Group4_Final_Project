package com.example.group4_final_project.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {


    @Length(min = 2,max = 20)
    private String firstName;


    @Length(min = 2,max = 20)
    private String lastName;


    @Length(min = 8)
    private String password;


}
