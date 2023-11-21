package com.example.group4_final_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.group4_final_project.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {


    Role findByName(String name);
}
