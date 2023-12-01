package com.example.group4_final_project.models.filtering;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class FilterOptionsUser {


    private Optional<String> email;

    private Optional<String> firstName;
    private Optional<String> lastName;


    public FilterOptionsUser(String email, String firstName, String lastName) {

        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
    }

}