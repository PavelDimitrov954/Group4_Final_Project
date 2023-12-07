package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.services.contracts.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class CourseMvcController {

    private final CourseService courseService;

    @Autowired
    public CourseMvcController(CourseService courseService) {
        this.courseService = courseService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping("/courses")
    public String showCoursePage(Model model) {
        var courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        return "courses";
    }

    @GetMapping("/course-details")
    public String showCourse() {
        return "course-details";
    }

}
