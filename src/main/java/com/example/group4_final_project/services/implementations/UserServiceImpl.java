package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.CourseMapper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.*;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.repositories.*;
import com.example.group4_final_project.services.contracts.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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
    private final SubmissionRepository submissionRepository;
    private  final CourseMapper courseMapper;



    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, SubmissionRepository submissionRepository, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.submissionRepository = submissionRepository;
        this.courseMapper = courseMapper;
    }


    @Override
    public ResponseUser get(int id, User loginUser) {
        if (loginUser.getId() != id) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }

        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User",id));

        if(user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT))){
            Optional<Enrollment> enrollments = enrollmentRepository.findEnrollmentByStudent(user);
            List<CourseDto> courses = new ArrayList<>();

            enrollments.stream().forEach(en->{
                CourseDto courseDto = courseMapper.toDto(en.getCourse());
              courses.add(courseDto);
            });

            ResponseUserStudent userStudent = userMapper.toResponseUserStudent(user);
            userStudent.setCourse(courses);
            return userStudent;

        }
        else if ((user.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER)))){
            List<CourseDto> courses = courseRepository.findAllByTeacher(user).stream().map(courseMapper::toDto).toList();
            ResponseUserTeacher userTeacher = userMapper.toResponseUserTeacher(user);
            userTeacher.setCourse(courses);
            return userTeacher;

        }

        return new ResponseUser(user.getFirstName(),user.getLastName(),user.getEmail());
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
    public ResponseUser addPicture(User user, String url) {

        user.setImageURL(url);
        userRepository.save(user);
        return userMapper.fromUser(user);
    }

    @Override
    public void approvedTeacher(int id, User loginUser) {
        /*if(loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))){
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }*/
        User user = userRepository.findById( id).orElseThrow(()->new EntityNotFoundException("User",id));
       Set<Role> roles =  user.getRoles();
       roles.remove(roleRepository.findByRoleName(RoleName.UNAPPROVED_TEACHER));
       roles.add(roleRepository.findByRoleName(RoleName.TEACHER));
       user.setRoles(roles);
       userRepository.save(user);


    }

    public List<GradeDto> getGrades(int id, User loginUser){
        if (loginUser.getId() != id) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));

        if(!user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT))){
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        List<GradeDto> gradeDto = new ArrayList<>();


        List<Submission> submissionList = submissionRepository.findAllByUser(user).stream().toList();


     return null;



    }





}
