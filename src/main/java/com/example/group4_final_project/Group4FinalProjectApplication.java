package com.example.group4_final_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories("com.example.group4_final_project.repositories")
public class Group4FinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(Group4FinalProjectApplication.class, args);
    }

}
