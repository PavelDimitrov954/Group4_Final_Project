package com.example.group4_final_project.models;

import com.example.group4_final_project.models.Lecture.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

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
