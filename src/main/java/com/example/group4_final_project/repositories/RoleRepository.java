package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {


    Role findByRoleName(RoleName roleName);

    Set<Role> findAllByIdGreaterThan(int id);
}
