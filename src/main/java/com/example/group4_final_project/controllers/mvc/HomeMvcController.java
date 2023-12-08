package com.example.group4_final_project.controllers.mvc;
import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.services.contracts.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final CourseService courseService;

    public HomeMvcController(CourseService courseService) {
        this.courseService = courseService;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }



    @GetMapping
    public String showHomePage(Model model) {
        List<CourseDtoView> courses = courseService.getAllCourses(); // Fetch courses from the service
        model.addAttribute("courses", courses);

        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}

