package com.example.group4_final_project.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int id;

    @ManyToOne
    @JoinTable(
            name = "lectures",
            joinColumns = @JoinColumn(name = "lecture_id")
    )
    private Lecture lecture; // Assuming there's a Lecture entity

    @Column(nullable = false)
    private String title;
    @Lob //for larger files!
    private String description;

    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;

    //TODO Should modified_at be added as field?
    public Assignment(int id,
                      Lecture lecture,
                      String title,
                      String description) {
        this.id = id;
        this.lecture = lecture;
        this.title = title;
        this.description = description;
    }

    public Assignment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getLecture(), that.getLecture()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getLecture(),
                getTitle(),
                getDescription(),
                getCreatedAt());
    }
}
