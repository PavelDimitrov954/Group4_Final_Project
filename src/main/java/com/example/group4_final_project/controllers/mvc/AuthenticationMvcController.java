package com.example.group4_final_project.controllers.mvc;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.UserLoginDto;
import com.example.group4_final_project.models.DTOs.UserRegisterDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {


    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       UserMapper userMapper, RoleRepository roleRepository) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new UserLoginDto());
        model.addAttribute("register", new UserRegisterDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") UserLoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {

            authenticationHelper.verifyAuthentication(login.getEmail(), login.getPassword());

            session.setAttribute("currentUser", login.getEmail());
            User user = authenticationHelper.tryGetCurrentUser(session);
           session.setAttribute("currentUserName", String.format("%s %s", user.getFirstName(),user.getLastName()));
           session.setAttribute("isStudent", user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT)));
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") UserRegisterDto register,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

       /* if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "register";
        }*/

        try {

            userService.register(register);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            String exceptionMessage = e.getMessage();
            if (exceptionMessage != null && exceptionMessage.contains("email")) {
                bindingResult.rejectValue("email", "email_error", exceptionMessage);
            } else if (exceptionMessage != null && exceptionMessage.contains("username")) {
                bindingResult.rejectValue("username", "username_error", exceptionMessage);
            }
            return "register";
        }
    }







}


