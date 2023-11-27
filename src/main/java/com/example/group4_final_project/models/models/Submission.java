package com.example.group4_final_project.models.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

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
    @JoinColumn(name="user_id", nullable = false)
    private User user;

  @Column(name = "submission_file")
  private File submissionFile;

    @Column(name = "grade")
    private BigDecimal grade;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "assignment_submissions",
            joinColumns = @JoinColumn(name = "assignment_id")

    )
    Set<Assignment> assignmentSet;

    @Column(name = "submitted_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant submittedAt;
}
