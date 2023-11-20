package com.example.group4_final_project.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;
    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer courseId) {
        this.id = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CourseTopic getTopic() {
        return topic;
    }

    public void setTopic(CourseTopic topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

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
