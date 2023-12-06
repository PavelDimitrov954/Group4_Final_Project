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

    private String imageURL;

    public ResponseUser(String firstName, String lastName, String email, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageURL = imageURL;
    }
}
