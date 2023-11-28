package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


  User  findByEmail(String email);



  @Query("SELECT u FROM User u " +
          "WHERE (:firstName is null or u.firstName like  LOWER(CONCAT(:firstName, '%'))) " +
          "AND (:lastName is null or u.lastName like  LOWER(CONCAT(:lastName, '%'))) " +
          "AND (:email is null or u.email like LOWER(CONCAT(:email, '%')))")
  List<User> findUsersByParameters(
          @Param("firstName") String firstName,
          @Param("lastName") String lastName,
          @Param("email") String email
  );




}
