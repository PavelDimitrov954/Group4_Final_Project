package com.example.group4_final_project.services;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.*;
import com.example.group4_final_project.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    public static final String INVALID_AUTHORIZATION = "Invalid authorization";
    public static final String TEACHER_ROLE_NAME = "teacher";
    public static final String UNAPPROVED_TEACHER_ROLE_NAME = "unapproved_teacher";

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User getByEmil(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.get(0);
    }

    @Override
    public User get(int id, User user) {
        if (user.getId() != id) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public List<User> get(FilterOptionsUser filterOptionsUser) {
        if (!filterOptionsUser.getFirstName().isPresent() && !filterOptionsUser.getEmail().isPresent()) {
            return userRepository.findAllByEmailAndFirstName(filterOptionsUser.getEmail().toString()
                    , filterOptionsUser.getFirstName().toString());
        } else if (!filterOptionsUser.getEmail().isPresent()) {
            return userRepository.findByEmail(filterOptionsUser.getEmail().toString());
        } else if (!filterOptionsUser.getFirstName().isPresent()) {
            return userRepository.findAllByFirstName(filterOptionsUser.getFirstName().toString());

        }
        return userRepository.findAll();
    }

    @Override
    public ResponseUserDto register(UserRegisterDto user) {
        User registerUser = userMapper.fromDto(user);
        if(!userRepository.findByEmail(registerUser.getEmail()).isEmpty()){
            throw new EntityDuplicateException("User", "email", registerUser.getEmail());
        }
        Role role;
        if(user.getRoleName().equals(TEACHER_ROLE_NAME)){
            role =  roleService.findByName(UNAPPROVED_TEACHER_ROLE_NAME);

        } else{
            role = roleService.findByName(user.getRoleName());
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        registerUser.setRoles(roles);
        userRepository.save(registerUser);
        return userMapper.fromUser(registerUser);
    }

    @Override
    public ResponseUserDto update(User user, int id, UserUpdateDto userUpdateDto) {
        boolean isDuplicate = true;

        User updateUser = userMapper.fromDto(id, userUpdateDto);
        if (user.getId() != id || !user.getEmail().equals(updateUser.getEmail())) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        try {
            User user1 = getByEmil(updateUser.getEmail());
            if (user1.getId() == updateUser.getId()) {
                isDuplicate = false;
            }

        } catch (EntityNotFoundException e) {
            isDuplicate = false;
        }
        if (!isDuplicate) {
            userRepository.save(updateUser);
            return userMapper.fromUser(updateUser);
        } else {
            throw new EntityDuplicateException("User", "email", updateUser.getEmail());
        }
    }

    @Override
    public ResponseUserDto delete(int id, User user) {
        User deleteUser = get(id, user);
        if (user.getId() != id || !user.getEmail().equals(deleteUser.getEmail())) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        ResponseUserDto responseUserDto = userMapper.fromUser(deleteUser);
        userRepository.delete(deleteUser);

        return responseUserDto;
    }


}
