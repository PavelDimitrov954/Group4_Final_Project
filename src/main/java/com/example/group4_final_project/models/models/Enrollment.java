package com.example.group4_final_project.models.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;


    @Column(nullable = false, updatable = false)
    private Timestamp enrolledAt;

    public Enrollment() {

    }

    public Enrollment(Course course, User student, Timestamp enrolledAt) {
        this.course = course;
        this.student = student;
        this.enrolledAt = enrolledAt;
    }
}
