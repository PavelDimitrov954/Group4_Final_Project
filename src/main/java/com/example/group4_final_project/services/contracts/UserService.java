package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.UserRegisterDto;
import com.example.group4_final_project.models.DTOs.UserUpdateDto;
import com.example.group4_final_project.models.ResponseUser;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.User;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {


    ResponseUser get(int id, User user);

    Page<ResponseUser> get(FilterOptionsUser filterOptionsUser, Pageable pageable);

    ResponseUser register(UserRegisterDto user);

    ResponseUser update(User user, int id, UserUpdateDto userRegisterDto);

    ResponseUser delete(int id, User user) throws AuthenticationException;


    void enrollCourse(User user, int id);

    User addPicture(User user, String url);
}
