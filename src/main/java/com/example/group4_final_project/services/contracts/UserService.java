package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.User;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface UserService {


    ResponseUser get(int id);


    List<ResponseUser> get(FilterOptionsUser filterOptionsUser, User loginUser);


    ResponseUser register(UserRegisterDto user);

    ResponseUser update(User user, int id, UserUpdateDto userRegisterDto) throws IOException;

    ResponseUser delete(int id, User user) throws AuthenticationException;


    void enrollCourse(User user, int id);

    ResponseUser addPicture(User user, String url);

    void approvedTeacher(int id, User user);
    public List<GradeDto> getGrades(int id, User loginUser);

    void makeUserAdmin(User admin, int id);

    Integer countByRole(RoleName roleName);
    boolean isTeacher(User user);
    boolean isStudent(User user);

    List<ResponseUser> getUnapprovedTeacher();
}
