package com.example.group4_final_project.services;

import com.example.group4_final_project.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import com.example.group4_final_project.models.Role;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;


    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void save(String name) {
        Role role = new Role();
        role.setName(name);
        roleRepository.save(role);
    }
}
