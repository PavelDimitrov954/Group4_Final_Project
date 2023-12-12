package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.DTOs.WikiPageDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.LectureService;
import com.example.group4_final_project.services.contracts.SubmissionService;
import com.example.group4_final_project.services.contracts.WikiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class CourseMvcController {

    private final UserMapper userMapper;

    private final AuthenticationHelper authenticationHelper;
    private final CourseService courseService;
    private final LectureService lectureService;
    private final SubmissionService submissionService;
   private final RoleRepository roleRepository;

    private final WikiService wikiService;

    public CourseMvcController(UserMapper userMapper, AuthenticationHelper authenticationHelper,
                               CourseService courseService, LectureService lectureService, SubmissionService submissionService, RoleRepository roleRepository, WikiService wikiService) {
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.submissionService = submissionService;
        this.roleRepository = roleRepository;
        this.wikiService = wikiService;
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


    @GetMapping("/courses")
    public String showCoursePage(Model model) {
        var courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";

    }


    @GetMapping("/courses/{courseId}/details")
    public String getCourseDetails(@PathVariable Integer courseId, Model model) {
        var course = courseService.getById(courseId);
        List<LectureDto> lectures = lectureService.getLecturesByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("courseTitle", course.getTitle()); // Assuming getTitle() gets the course title
        model.addAttribute("lectures", lectures);
        if (!model.containsAttribute("searchResults")) {
            model.addAttribute("searchResults", new ArrayList<WikiPageDto>()); // Add an empty list if no search was performed
        }
        return "course-details";
    }



    @PostMapping("/lectures/{lectureId}/upload")
    public String uploadAssignment(@PathVariable Integer lectureId,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            String fileUrl = submissionService.handleAssignmentUpload(lectureId, user.getId(), file);

            // Adding more details to the success message
            String successMessage = String.format("File '%s' uploaded successfully! Size: %d bytes. URL: %s",
                    file.getOriginalFilename(), file.getSize(), fileUrl);
            redirectAttributes.addFlashAttribute("message", successMessage);

            redirectAttributes.addFlashAttribute("message", "File uploaded successfully! URL: " + fileUrl);
        } catch (EntityNotFoundException | IOException e) {
            redirectAttributes.addFlashAttribute("message", "Upload failed: " + e.getMessage());
            return "redirect:/error"; // or an appropriate error page
        } catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("message", "Access denied: " + e.getMessage());
            return "redirect:/access-denied"; // or an appropriate access denied page
        }

        redirectAttributes.addAttribute("courseId", lectureService.getLectureById(lectureId).getCourseId());
        return "redirect:/courses/{courseId}/details";
    }

    @GetMapping("/search-wikipedia")
    public String searchWikipedia(@RequestParam String searchTerm, Model model) {
        List<WikiPageDto> searchResults = wikiService.searchWikipedia(searchTerm);
        model.addAttribute("searchResults", searchResults);
        return "fragments/searchResults"; // Path to the Thymeleaf fragment containing search results
    }

}
