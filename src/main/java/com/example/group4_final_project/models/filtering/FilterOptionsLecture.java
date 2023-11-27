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

    public FilterOptionsLecture(Integer courseId, String title, String description) {
        this.courseId = Optional.ofNullable(courseId);
        this.title = Optional.ofNullable(title);
        this.description = Optional.ofNullable(description);
    }
}
