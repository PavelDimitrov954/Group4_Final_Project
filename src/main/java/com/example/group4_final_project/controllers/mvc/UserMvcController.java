package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping()
    public String showProfile(HttpSession session, Model model) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        model.addAttribute("user", userService.get(user.getId(), user));
        model.addAttribute("courses", courseService.getAllCoursesBYUser(user));
        System.out.println("HELLO");
        return "profile";

    }

}
