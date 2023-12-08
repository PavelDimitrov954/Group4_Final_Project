package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AuthenticationHelper(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        String email = getEmail(userInfo);
        String password = getPassword(userInfo);

        return verifyAuthentication(email, password);
    }

    public User tryGetCurrentUser(HttpSession session) {

        String email = (String) session.getAttribute("currentUser");

        if (email == null) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userRepository.findByEmail(email);
    }


    public User verifyAuthentication(String email, String password) {
        try {

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getEmail(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }
}

