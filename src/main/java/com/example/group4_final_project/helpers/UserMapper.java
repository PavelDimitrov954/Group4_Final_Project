package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.ResponseUserDto;
import com.example.group4_final_project.models.User;
import com.example.group4_final_project.models.UserRegisterDto;
import com.example.group4_final_project.models.UserUpdateDto;
import com.example.group4_final_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserMapper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;




@Autowired
    public UserMapper(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;


    this.passwordEncoder = passwordEncoder;
}

    public User fromDto(UserRegisterDto userRegisterDto) {

        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        return user;
    }



    public User fromDto(int id, UserUpdateDto userUpdateDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));

        user.setPassword(userUpdateDto.getPassword());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setUpdatedAt(Instant.now());
        return user;
    }

    public ResponseUserDto fromUser(User user) {

        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setFirstName(user.getFirstName());
        responseUserDto.setLastName(user.getLastName());
        responseUserDto.setEmail(user.getEmail());
        return responseUserDto;
    }
}
