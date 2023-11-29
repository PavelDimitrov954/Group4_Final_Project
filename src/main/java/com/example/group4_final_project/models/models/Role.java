package com.example.group4_final_project.models.models;

import com.example.group4_final_project.models.enums.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;



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
    private RoleName roleName ;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }
}
