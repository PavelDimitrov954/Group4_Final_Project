package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.exceptions.UnauthorizedOperationException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")

public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final CourseService courseService;
    private final RoleRepository roleRepository;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper, CourseService courseService, RoleRepository roleRepository) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.courseService = courseService;
        this.roleRepository = roleRepository;
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
        return roleRepository.findByRoleName(RoleName.ADMIN);

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
    @GetMapping("/{id}/grades")
    public String grades(@PathVariable int id, HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

           List<GradeDto> gradeDtoList =  userService.getGrades(id,user);
           model.addAttribute("grades",gradeDtoList);

            return "grades";

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }
    @GetMapping("/{id}/makeAdmin")
    public String makeUserAdmin(Model model, @PathVariable int id, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetCurrentUser(session);

            userService.makeUserAdmin(admin, id);
            return "redirect:/users/{id}";

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

    @GetMapping("/search")
    public String Users(@ModelAttribute("filterOptions")  FilterDtoUser filterDtoUser,
                           HttpSession session,Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);



                model.addAttribute("filterOptions", filterDtoUser);
                model.addAttribute("users", new ArrayList<ResponseUser>());
                return "search";



        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


    }



    @GetMapping("/view")
    public String filterUsers(@ModelAttribute("filterOptions")  FilterDtoUser filterDtoUser,
                           HttpSession session,Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);


            FilterOptionsUser filterOptionsUser = new FilterOptionsUser(filterDtoUser.getEmail(),
                    filterDtoUser.getFirstName(),
                    filterDtoUser.getLastName());
            List<ResponseUser> users = userService.get(filterOptionsUser,user);
            model.addAttribute("filterOptions", filterDtoUser);
            model.addAttribute("users", users);
            return "search";



        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

    @GetMapping("/submission")
    public String studentSubmission(Model model,
                              HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            List<SubmissionDto>  submissionDtoList = userService.getStudentSubmission(user);
            model.addAttribute("submission",submissionDtoList );

            return "submission";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }
}
