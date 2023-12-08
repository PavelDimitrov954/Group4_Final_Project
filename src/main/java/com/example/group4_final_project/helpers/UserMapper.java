package com.example.group4_final_project.helpers;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.models.User;
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
        if(!userUpdateDto.getFirstName().trim().isEmpty()){
            user.setFirstName(userUpdateDto.getFirstName());
        }
        if(!userUpdateDto.getLastName().trim().isEmpty()){
            user.setLastName(userUpdateDto.getLastName());
        }

        user.setUpdatedAt(Instant.now());
        return user;
    }
    public User fromDto(ResponseUser responseUser) {


        return userRepository.findByEmail(responseUser.getEmail());
    }

    public ResponseUser fromUser(User user) {

        ResponseUser responseUser = new ResponseUser();
        responseUser.setId(user.getId());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        responseUser.setEmail(user.getEmail());
        responseUser.setImageURL(user.getImageURL());
        responseUser.setRoles(user.getRoles());
        return responseUser;
    }




    public UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(user.getId());
        userUpdateDto.setFirstName(user.getFirstName());
        userUpdateDto.setLastName(user.getLastName());
        userUpdateDto.setPassword(user.getPassword());
       // userUpdateDto.setImageURL(user.getImageURL());
     return userUpdateDto;
    }
}
