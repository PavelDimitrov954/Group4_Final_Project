package com.example.group4_final_project.services;

import com.example.group4_final_project.models.*;
import org.apache.tomcat.websocket.AuthenticationException;

import java.util.List;

public interface UserService {
    User getByEmil(String email);

    User get(int id, User user);
    List<User> get(FilterOptionsUser filterOptionsUser);

    ResponseUserDto register(UserRegisterDto user);

    ResponseUserDto update(User user, int id, UserUpdateDto userRegisterDto);

    ResponseUserDto delete(int id, User user) throws AuthenticationException;




}
