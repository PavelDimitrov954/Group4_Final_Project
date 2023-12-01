package com.example.group4_final_project.models.filtering;

import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class FilterOptionsCourse {
    //    The user must be able to filter the catalog by course name, course topic, teacher, and rating
    private Optional<String> courseTitle;
    private Optional<CourseTopic> courseTopic;
    private Optional<User> teacher;
    private Optional<Integer> rating;

    public FilterOptionsCourse(String courseTitle, CourseTopic courseTopic, User teacher, Integer rating) {
        this.courseTitle = Optional.ofNullable(courseTitle);
        this.courseTopic = Optional.ofNullable(courseTopic);
        this.teacher = Optional.ofNullable(teacher);
        this.rating = Optional.ofNullable(rating);
    }

}
