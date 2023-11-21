package com.example.group4_final_project.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
@Getter
@Setter
public class FilterOptionsUser {


    private Optional<String> email;

    private Optional<String> firstName;


    public FilterOptionsUser(String email, String firstName) {

        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
    }

}