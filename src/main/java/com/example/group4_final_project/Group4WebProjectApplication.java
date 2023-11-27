package com.example.group4_final_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.example.group4_final_project.repositories")
public class Group4WebProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(Group4WebProjectApplication.class, args);
    }

}
