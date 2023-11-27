package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.ResponseUser;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.models.DTOs.UserRegisterDto;
import com.example.group4_final_project.models.DTOs.UserUpdateDto;
import com.example.group4_final_project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserMapper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Autowired
    public UserMapper(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User fromDto(UserUpdateDto userUpdateDto) {

        User user = userRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("User", 1));
        User updateUser = modelMapper.map(userUpdateDto, User.class);
        updateUser.setId(user.getId());


     return updateUser;

    }

    public User fromDto(UserRegisterDto userRegisterDto) {


        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        return user;
    }


    public User fromDto(int id, UserUpdateDto userUpdateDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setUpdatedAt(Instant.now());
        return user;
    }

    public ResponseUser fromUser(User user) {

        ResponseUser responseUserDto = new ResponseUser();
        responseUserDto.setFirstName(user.getFirstName());
        responseUserDto.setLastName(user.getLastName());
        responseUserDto.setEmail(user.getEmail());
        return responseUserDto;
    }
}
