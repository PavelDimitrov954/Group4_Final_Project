package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Id> {

     User getUserById(int id);

     List<User> getAll();

     void deleteById(int id);

     User  save(User user);

     void  update(User user);



}
