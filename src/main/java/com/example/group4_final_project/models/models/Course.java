package com.example.group4_final_project.models.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private CourseTopic topic;

    @Lob
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;


    @Column
    private int rating;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    private Set<Enrollment> enrollments;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(id, course.id) &&
                Objects.equals(title, course.title) &&
                Objects.equals(topic, course.topic) &&
                Objects.equals(description, course.description) &&
                Objects.equals(startDate, course.startDate) &&
                Objects.equals(status, course.status) &&
                Objects.equals(teacher, course.teacher) &&
                Objects.equals(createdAt, course.createdAt) &&
                Objects.equals(updatedAt, course.updatedAt) &&
                Objects.equals(enrollments, course.enrollments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                title,
                topic,
                description,
                startDate,
                status,
                teacher,
                createdAt,
                updatedAt,
                enrollments);
    }
}
