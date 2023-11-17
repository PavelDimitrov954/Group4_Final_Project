package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
     void getUserById(int id);
     void deleteById(int id);

}
