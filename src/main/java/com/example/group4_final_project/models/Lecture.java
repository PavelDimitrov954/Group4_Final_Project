package com.example.group4_final_project.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NonNull
    private Course course;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String description;

    @Column(name = "video_link")
    private String videoLink;

    @Column(nullable = false, length = 50)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

}
