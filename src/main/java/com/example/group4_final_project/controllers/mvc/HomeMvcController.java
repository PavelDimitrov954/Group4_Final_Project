package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public HomeMvcController(AuthenticationHelper authenticationHelper, RoleRepository roleRepository, UserMapper userMapper, CourseService courseService, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.courseService = courseService;
        this.userService = userService;
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

    @ModelAttribute("student")
    public Role RoleStudent() {
        return roleRepository.findByRoleName(RoleName.STUDENT);

    }

    @ModelAttribute("teacher")
    public Role RoleTeacher() {
        return roleRepository.findByRoleName(RoleName.TEACHER);

    }

    @ModelAttribute("admin")
    public Role RoleAdmin() {
        return roleRepository.findByRoleName(RoleName.STUDENT);

    }

    @GetMapping
    public String showHomePage(Model model) {
        List<CourseDtoView> courses = courseService.getAllCourses(); // Fetch courses from the service
        int studentCount = userService.countByRole(RoleName.STUDENT); // Assume this method exists
        int teacherCount = userService.countByRole(RoleName.TEACHER); // Assume this method exists
        int courseCount = courses.size(); // You can also have a dedicated method for counting courses

        model.addAttribute("courses", courses);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("teacherCount", teacherCount);
        model.addAttribute("courseCount", courseCount);
        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }


    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }
}

