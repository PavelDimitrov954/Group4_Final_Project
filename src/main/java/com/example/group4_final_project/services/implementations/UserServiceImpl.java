package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.UserRegisterDto;
import com.example.group4_final_project.models.DTOs.UserUpdateDto;
import com.example.group4_final_project.models.ResponseUser;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Enrollment;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.repositories.CourseRepository;
import com.example.group4_final_project.repositories.EnrollmentRepository;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.repositories.UserRepository;
import com.example.group4_final_project.services.contracts.UserService;
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
    public Page<ResponseUser> get(FilterOptionsUser filterOptionsUser, Pageable pageable) {
        String firstName = filterOptionsUser.getFirstName().isPresent()
                ? filterOptionsUser.getFirstName().get() : null;

        String lastName = filterOptionsUser.getLastName().isPresent()
                ? filterOptionsUser.getLastName().get() : null;

        String email = filterOptionsUser.getEmail().isPresent()
                ? filterOptionsUser.getEmail().get() : null;

List<ResponseUser>  responseUsers = userRepository.findUsersByParameters(firstName,
        lastName, email).stream().map(userMapper::fromUser).collect(Collectors.toList());
      Page page = new PageImpl(responseUsers);

     return page;
    }


    @Override
    public ResponseUser register(UserRegisterDto user) {
        User registerUser = userMapper.fromDto(user);
        registerUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByEmail(registerUser.getEmail()) != null) {
            throw new EntityDuplicateException("User", "email", registerUser.getEmail());
        }
        Role role = roleRepository.findByRoleName(RoleName.valueOf(user.getRoleName().toUpperCase()));

        if (role == null) {
          throw new EntityNotFoundException("Role","RoleName",user.getRoleName().toUpperCase());
        }

        if (role.getRoleName().equals(RoleName.TEACHER)) {
          role = roleRepository.findByRoleName(RoleName.UNAPPROVED_TEACHER) ;
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
        Role role = roleRepository.findByRoleName(RoleName.STUDENT);

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

    @Override
    public User addPicture(User user, String url) {

        user.setImageURL(url);
        userRepository.save(user);
        return user;
    }


}
