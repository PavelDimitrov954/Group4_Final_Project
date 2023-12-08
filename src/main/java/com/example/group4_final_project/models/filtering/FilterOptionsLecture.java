package com.example.group4_final_project.models.filtering;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class FilterOptionsLecture {

    private Optional<Integer> courseId;
    private Optional<String> title;
    private Optional<String> description;
    private String sortBy;
    private boolean asc;

    public FilterOptionsLecture(Integer courseId, String title, String description, String sortBy, boolean asc) {
        this.courseId = Optional.ofNullable(courseId);
        this.title = Optional.ofNullable(title);
        this.description = Optional.ofNullable(description);
        this.sortBy = sortBy;
        this.asc = asc;
    }
}