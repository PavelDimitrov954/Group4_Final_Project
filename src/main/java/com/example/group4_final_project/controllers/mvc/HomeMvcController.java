package com.example.group4_final_project.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {


    @GetMapping
    public String showHomePage() {


        return "index";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

}