package com.example.group4_final_project.controllers.mvc;
import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.repositories.RoleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public HomeMvcController(AuthenticationHelper authenticationHelper, RoleRepository roleRepository, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("responseUser")
    public ResponseUser responseUser(HttpSession session) {
        try {
            return userMapper.fromUser(authenticationHelper.tryGetCurrentUser(session));
        }catch (EntityNotFoundException | AuthorizationException e){
            return new ResponseUser();
        }


    }



    @GetMapping
    public String showHomePage() {


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
