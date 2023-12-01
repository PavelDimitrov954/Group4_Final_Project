package com.example.group4_final_project.models.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "submission_Url")
    private String submissionURL;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "submitted_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant submittedAt;
}
