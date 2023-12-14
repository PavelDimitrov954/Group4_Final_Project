package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.services.contracts.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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
    private final UserService userService;
    private final WikiService wikiService;

    private final EnrollmentService enrollmentService;

    @Autowired
    public CourseMvcController(UserMapper userMapper, AuthenticationHelper authenticationHelper, CourseService courseService, LectureService lectureService, SubmissionService submissionService, RoleRepository roleRepository, UserService userService, WikiService wikiService, EnrollmentService enrollmentService) {
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.submissionService = submissionService;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.wikiService = wikiService;
        this.enrollmentService = enrollmentService;
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
    public String showCoursePage(@ModelAttribute("filterOptions") FilterDtoCourse filterDtoCourse, HttpSession session, Model model) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        boolean isTeacher = userService.isTeacher(user);

        FilterOptionsCourse filterOptionsCourse = new FilterOptionsCourse(filterDtoCourse.getCourseTitle(), filterDtoCourse.getCourseTopic(), filterDtoCourse.getTeacher(), filterDtoCourse.getRating());

        var courses = courseService.getAllCourses(filterOptionsCourse);
        var topics = courseService.getAllCourseTopics();

        model.addAttribute("isTeacher", isTeacher);
        model.addAttribute("courses", courses);
        model.addAttribute("courseTopics", topics);

        return "courses";

    }

    @GetMapping("/courses/create")
    public String createCourse(Model model) {

        model.addAttribute("courseDto", new CreateCourseDto());
        model.addAttribute("topics", courseService.getAllCourseTopics());

        return "create-course";
    }

    @PostMapping("/courses/create")
    public String createCourse(@ModelAttribute("courseDto") CreateCourseDto courseDto, HttpSession session) {
        // Add logic to save the course using the provided DTO
        // Example:
        User userWhoCreates = authenticationHelper.tryGetCurrentUser(session);

        courseService.createCourse(userWhoCreates, courseDto);

        // Redirect to a page after successful course creation
        return "redirect:/courses";
    }


    @GetMapping("/courses/update/{courseId}")
    public String getUpdateCoursePage(@PathVariable int courseId, Model model, HttpSession session) {
        User userWhoUpdates = authenticationHelper.tryGetCurrentUser(session);

        // Get the course and check if the user is the teacher
        CourseDtoView course = courseService.getById(courseId);
        if (!course.getTeacher().getEmail().equals(userWhoUpdates.getEmail())) {
            // Handle unauthorized access
            return "redirect:/error";  // You can create a custom error page
        }

        // Populate the model with necessary data (e.g., topics)
        List<CourseTopicDto> topics = courseService.getAllCourseTopics();
        model.addAttribute("topics", topics);
        model.addAttribute("updateCourseDto", new UpdateCourseDto());
        model.addAttribute("course", course);

        return "update-course";
    }

    @PostMapping("/courses/update/{courseId}")
    public String updateCourse(@ModelAttribute("updateCourseDto") UpdateCourseDto updateCourseDto, @PathVariable int courseId, HttpSession session) {
        // Validate and update the course in the database
        User userWhoUpdates = authenticationHelper.tryGetCurrentUser(session);
        CourseDtoView updatedCourse = courseService.updateCourse(userWhoUpdates, courseId, updateCourseDto);

        // Redirect to the updated course details page
        String redirect = String.format("redirect:/courses/%s/details/", courseId);
        return redirect;
    }

    // Other methods...


    @GetMapping("/courses/{courseId}/details")
    public String getCourseDetails(@PathVariable Integer courseId, Model model, HttpSession session) {
        var course = courseService.getById(courseId);

        User user = authenticationHelper.tryGetCurrentUser(session);
        boolean isTeacher = userService.isTeacher(user);
        boolean isStudent = userService.isStudent(user);
        List<LectureDto> lectures = lectureService.getLecturesByCourseId(courseId);
        boolean isEnrolled = false;
        if (isStudent) {
            isEnrolled = enrollmentService.isStudentEnrolled(user.getId(), courseId);
        }
        model.addAttribute("isTeacher", isTeacher);
        model.addAttribute("isStudent", isStudent);
        model.addAttribute("course", course);
        model.addAttribute("courseTitle", course.getTitle()); // Assuming getTitle() gets the course title
        model.addAttribute("lectures", lectures);
        model.addAttribute("isEnrolled", isEnrolled);
        if (!model.containsAttribute("searchResults")) {
            model.addAttribute("searchResults", new ArrayList<WikiPageDto>()); // Add an empty list if no search was performed
        }
        return "course-details";
    }


    @PostMapping("/lectures/{lectureId}/upload")
    public String uploadAssignment(@PathVariable Integer lectureId, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            String fileUrl = submissionService.handleAssignmentUpload(lectureId, user.getId(), file);

            // Adding more details to the success message
            String successMessage = String.format("File '%s' uploaded successfully! Size: %d bytes. URL: %s", file.getOriginalFilename(), file.getSize(), fileUrl);
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

    @PostMapping("/courses/enroll/{id}")
    public String enrollCourse(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            userService.enrollCourse(user, id);


        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        String redirect = String.format("redirect:/courses/%s/details", id);
        return redirect;
    }

    @PostMapping("/courses/unenroll/{id}")
    public String unenrollCourse(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            enrollmentService.removeStudentFromCourse(user.getId(), id);


        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        String redirect = String.format("redirect:/courses/%s/details", id);
        return redirect;
    }
}
