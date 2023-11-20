package com.example.group4_final_project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_topics")
public class CourseTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    @Column(nullable = false, length = 255)
    private String name;


    public CourseTopic() {
    }

    public CourseTopic(String name) {
        this.name = name;
    }

}
