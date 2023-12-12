package com.example.group4_final_project.models.models;

import com.example.group4_final_project.models.enums.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName roleName;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return id == role.id && roleName == role.roleName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }
}
