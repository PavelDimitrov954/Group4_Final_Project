package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.models.DTOs.CreateCourseDto;
import com.example.group4_final_project.models.DTOs.UpdateCourseDto;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    private final CourseService courseService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public CourseRestController(CourseService courseService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;

    }

    @GetMapping
    public List<CourseDtoView> searchCourses(//@RequestHeader HttpHeaders headers,
                                             @RequestParam(required = false) String courseTitle,
                                             @RequestParam(required = false) CourseTopic courseTopic,
                                             @RequestParam(required = false) User teacher,
                                             @RequestParam(required = false) Integer rating) {
        try {
            FilterOptionsCourse filterOptions = new FilterOptionsCourse(courseTitle, courseTopic, teacher, rating);

            return courseService.getAllCourses(filterOptions);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CourseDtoView getCourse(@PathVariable int id) {
        //any validations?
        try {
            return courseService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public CourseDtoView createCourse(@RequestHeader HttpHeaders header,
                                      @RequestBody CreateCourseDto courseDto) {
        try {
            User userWhoCreates = authenticationHelper.tryGetUser(header);
            return
                    courseService.createCourse(userWhoCreates, courseDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CourseDtoView updateCourse(@RequestHeader HttpHeaders header,
                                      @PathVariable int id,
                                      @RequestBody UpdateCourseDto courseDto) {

        try {
            User userWhoUpdates = authenticationHelper.tryGetUser(header);
            return courseService.updateCourse(userWhoUpdates, id, courseDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@RequestHeader HttpHeaders header, @PathVariable int id) {
        try {
            User userWhoDeletes = authenticationHelper.tryGetUser(header);
            courseService.delete(userWhoDeletes, id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/enroll/{id}")
    public void enrollCourse(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.enrollCourse(user, id);


        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
