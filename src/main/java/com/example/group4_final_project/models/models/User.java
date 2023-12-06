package com.example.group4_final_project.models.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;


    @NotNull
    @Length(min = 2, max = 20)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Length(min = 2, max = 20)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotNull

    @Column(name = "password")
    private String password;

    @Column(name = "image_URL")
    private String imageURL;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<Role> roles;


    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;


    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    public User() {
        this.roles = new HashSet<>();
    }
}

