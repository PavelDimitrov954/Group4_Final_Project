package com.example.group4_final_project.models.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseUser {

    private String firstName;


    private String lastName;


    private String email;

    public ResponseUser(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
