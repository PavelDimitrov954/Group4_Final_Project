package com.example.group4_final_project.services;

import com.example.group4_final_project.models.Role;

public interface RoleService {
    Role findByName(String name);
    void save(String name);
}
