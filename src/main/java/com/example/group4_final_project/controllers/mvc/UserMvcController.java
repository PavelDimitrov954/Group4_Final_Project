package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.DTOs.UserUpdateDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.io.IOException;

@Controller
@RequestMapping("/users")

public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final CourseService courseService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper, CourseService courseService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.courseService = courseService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("responseUser")
    public ResponseUser responseUser(HttpSession session) {
        try {
            return userMapper.fromUser(authenticationHelper.tryGetCurrentUser(session));
        } catch (EntityNotFoundException | AuthorizationException e) {
            return new ResponseUser();
        }


    }

    @GetMapping()
    public String showProfile(HttpSession session, Model model) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        model.addAttribute("user", userService.get(user.getId()));
        model.addAttribute("courses", courseService.getAllCoursesBYUser(user));

        return "profile";

    }

    @GetMapping("/{id}/update")
    public String updateUser(Model model, @PathVariable int id, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            UserUpdateDto userUpdateDto = new UserUpdateDto();
            userUpdateDto.setId(id);


            model.addAttribute("user", userUpdateDto);
            return "updateUser";
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

    @PostMapping("/{id}/update")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDto updateUser, BindingResult bindingResult, @PathVariable int id,
                             HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "updateUser";
        }


        try {

            authenticationHelper.tryGetCurrentUser(session);
            User user = userMapper.fromDto(userService.get(id));
            userService.update(user, id, updateUser);

            return "redirect:/users";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "updateUser";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
             userService.delete(id,user);
            session.removeAttribute("currentUser");
            return "redirect:/";

        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }
}