package com.example.group4_final_project.services;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.*;
import com.example.group4_final_project.repositories.CourseRepository;
import com.example.group4_final_project.repositories.EnrollmentRepository;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public static final String INVALID_AUTHORIZATION = "Invalid authorization";
    public static final String TEACHER_ROLE_NAME = "teacher";
    public static final String UNAPPROVED_TEACHER_ROLE_NAME = "unapproved_teacher";
    public static final String ERROR_ENROLL_COURSE = "Only students can enroll this course";
    public static final String YOU_ARE_ALREADY_ENROLLED_IN_THIS_COURSE = "You are already enrolled in this course";


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;


    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }


    @Override
    public ResponseUser get(int id, User user) {
        if (user.getId() != id) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }

        User responseUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.fromUser(responseUser);
    }

    @Override
    public Page<User> get(FilterOptionsUser filterOptionsUser, Pageable pageable) {
        String firstName = filterOptionsUser.getFirstName().isPresent()
                ? filterOptionsUser.getFirstName().get() : null;

        String lastName = filterOptionsUser.getLastName().isPresent()
                ? filterOptionsUser.getLastName().get() : null;

        String email = filterOptionsUser.getEmail().isPresent()
                ? filterOptionsUser.getEmail().get() : null;

      return userRepository.findUsersByParameters(firstName,
                lastName, email, pageable);



    }


    @Override
    public ResponseUser register(UserRegisterDto user) {
        User registerUser = userMapper.fromDto(user);
        registerUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByEmail(registerUser.getEmail()) != null) {
            throw new EntityDuplicateException("User", "email", registerUser.getEmail());
        }
        Role role = roleRepository.findByName(user.getRoleName());

        if (role == null) {
            role = new Role(user.getRoleName());
            roleRepository.save(role);
        }

        if (user.getRoleName().equals(TEACHER_ROLE_NAME)) {
            role = roleRepository.findByName(UNAPPROVED_TEACHER_ROLE_NAME);
            if (role == null) {
                role = new Role(UNAPPROVED_TEACHER_ROLE_NAME);
                roleRepository.save(role);
            }
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        registerUser.setRoles(roles);

        userRepository.save(registerUser);
        return userMapper.fromUser(registerUser);
    }

    @Override
    public ResponseUser update(User user, int id, UserUpdateDto userUpdateDto) {
        boolean isDuplicate = true;

        User updateUser = userMapper.fromDto(id, userUpdateDto);
        updateUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));

        if (user.getId() != id || !user.getEmail().equals(updateUser.getEmail())) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        try {
            User user1 = userRepository.findByEmail(updateUser.getEmail());
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
    public ResponseUser delete(int id, User user) {
        User deleteUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        if (user.getId() != id || !user.getEmail().equals(deleteUser.getEmail())) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        ResponseUser responseUserDto = userMapper.fromUser(deleteUser);
        userRepository.delete(deleteUser);

        return responseUserDto;
    }

    @Override
    public void enrollCourse(User user, int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course", id));
        Role role = roleRepository.findByName("student");

        if (!user.getRoles().contains(role) || role == null) {
            throw new AuthorizationException(ERROR_ENROLL_COURSE);
        }

        if (course.getStartDate().after(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new IllegalArgumentException
                    (String.format("Course registration starts from %s", course.getStartDate().toString()));
        }
        Optional<Enrollment> enrollmentOptional =
                enrollmentRepository.findByStudentIdAndCourseId(user.getId(), id);

        if (enrollmentOptional.isPresent()) {
            throw new EntityDuplicateException(YOU_ARE_ALREADY_ENROLLED_IN_THIS_COURSE);
        }

        Enrollment enrollment = new Enrollment(course, user, Timestamp.valueOf(LocalDateTime.now()));
        enrollmentRepository.save(enrollment);


    }


}
