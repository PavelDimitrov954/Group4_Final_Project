package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.FilterOptionsUser;
import com.example.group4_final_project.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer>{


  List <User> findByEmail(String email);
  List<User> findAllByEmailAndFirstName(String email, String firstName);
  List <User> findAllByFirstName(String firstName);





}
