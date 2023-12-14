package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.exceptions.UnauthorizedOperationException;
import com.example.group4_final_project.helpers.CourseMapper;
import com.example.group4_final_project.helpers.ImageHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.*;
import com.example.group4_final_project.repositories.*;
import com.example.group4_final_project.services.contracts.CourseService;
import com.example.group4_final_project.services.contracts.LectureService;
import com.example.group4_final_project.services.contracts.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final CourseMapper courseMapper;

    private final CourseService courseService;
    private final ImageHelper imageHelper;
    private final LectureRepository lectureRepository;
    private final LectureService lectureService;


    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, SubmissionRepository submissionRepository, CourseMapper courseMapper, CourseService courseService, ImageHelper imageHelper, LectureRepository lectureRepository, LectureService lectureService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.submissionRepository = submissionRepository;
        this.courseMapper = courseMapper;
        this.courseService = courseService;
        this.imageHelper = imageHelper;
        this.lectureRepository = lectureRepository;
        this.lectureService = lectureService;
    }


    @Override
    public ResponseUser get(int id) {

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.fromUser(user);
    }


    @Override
    public List<ResponseUser> get(FilterOptionsUser filterOptionsUser, User loginUser) {
        if (!loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN)) &&
                !loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER))) {

            throw new UnauthorizedOperationException("Only teacher and  admin can by search users");
        }

        String firstName = filterOptionsUser.getFirstName().isPresent()
                ? filterOptionsUser.getFirstName().get() : null;

        String lastName = filterOptionsUser.getLastName().isPresent()
                ? filterOptionsUser.getLastName().get() : null;

        String email = filterOptionsUser.getEmail().isPresent()
                ? filterOptionsUser.getEmail().get() : null;

        List<ResponseUser> responseUsers = userRepository.findUsersByParameters(firstName,
                lastName, email).stream().map(userMapper::fromUser).collect(Collectors.toList());


        return responseUsers;
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
            throw new EntityNotFoundException("Role", "RoleName", user.getRoleName().toUpperCase());
        }

        if (role.getRoleName().equals(RoleName.TEACHER)) {
            role = roleRepository.findByRoleName(RoleName.UNAPPROVED_TEACHER);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        registerUser.setRoles(roles);

        userRepository.save(registerUser);
        return userMapper.fromUser(registerUser);
    }

    @Override
    public ResponseUser update(User user, int id, UserUpdateDto userUpdateDto) throws IOException {
        boolean isDuplicate = true;
        User updateUser = userMapper.fromDto(id, userUpdateDto);

        if (userUpdateDto.getPassword() != null) {
            updateUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        if (!userUpdateDto.getImage().isEmpty()) {
            System.out.println("2");
            user.setImageURL(imageHelper.uploadImage(userUpdateDto.getImage()));
        }

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
        if (user.getId() == id && user.getEmail().equals(deleteUser.getEmail())
                || user.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))) {
            ResponseUser responseUserDto = userMapper.fromUser(deleteUser);
            userRepository.delete(deleteUser);
            return responseUserDto;
        }

        throw new AuthorizationException(INVALID_AUTHORIZATION);
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
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));
        Set<Role> roles = user.getRoles();
        roles.remove(roleRepository.findByRoleName(RoleName.UNAPPROVED_TEACHER));
        roles.add(roleRepository.findByRoleName(RoleName.TEACHER));
        user.setRoles(roles);
        userRepository.save(user);


    }

    public List<GradeDto> getGrades(int id, User loginUser) {
        if (loginUser.getId() != id) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));

        if (!user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT))) {
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }
        List<GradeDto> gradesDtoList = new ArrayList<>();
        List<CourseDto> courses = courseService.getAllCoursesBYUser(user);


        if (courses.isEmpty()) {
            return gradesDtoList;
        }

        for (CourseDto course : courses) {
            GradeDto gradeDto = new GradeDto();
            gradeDto.setCourseTitle(course.getTitle());
            Map<Lecture, Double> grades = new LinkedHashMap<>();
            course.getLectures().stream().forEach(lecture -> {
                Submission submission =
                        submissionRepository.findByAssignmentAndAndUser(lecture.getAssignment(), user);
                grades.putIfAbsent(lecture, submission.getGrade());
                gradeDto.setGrades(grades);
            });

            double average = grades.values().stream()
                    .collect(Collectors.averagingDouble(Double::doubleValue));
            gradeDto.setAbvGrade(average);

            gradesDtoList.add(gradeDto);
        }


        return gradesDtoList;


    }

    @Override
    public void makeUserAdmin(User admin, int id) {
        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN);
        if (!admin.getRoles().contains(adminRole)) {
            throw new AuthorizationException("You don't have permission to make users admin.");
        }

        User userToPromote = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));

        if (userToPromote.getRoles().contains(adminRole)) {
            throw new EntityDuplicateException("User is already an admin: " + userToPromote.getEmail());
        }

        Set<Role> roles = userToPromote.getRoles();
        roles.add(adminRole);
        userToPromote.setRoles(roles);

        userRepository.save(userToPromote);

    }

    @Override
    public List<SubmissionDto> getStudentSubmission(User loginUser) {
        if (!loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN)) &&
                !loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER))) {

            throw new UnauthorizedOperationException("Only teacher and  admin can by search users");
        }

        List<Course> courses = courseRepository.findAllByTeacher(loginUser).stream().toList();
        List<Lecture> lectureLIst = new ArrayList<>();
        courses.stream().forEach(course -> {
            lectureLIst.addAll(lectureRepository.findAllByCourse(course).stream().toList());
        });
        List<SubmissionDto> submissionDtoList = new ArrayList<>();

        lectureLIst.stream().forEach(lecture -> {

            SubmissionDto submissionDto = new SubmissionDto();
            submissionDto.setLecture(lecture);
            submissionDto.setSubmission(submissionRepository.findByAssignment(lecture.getAssignment())
                    .stream()
                    .filter(submission -> submission.getGrade() != null).collect(Collectors.toList()));
            submissionDtoList.add(submissionDto);
        });

        return submissionDtoList;
    }

    @Override
    public boolean isTeacher(User user) {
        return user.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER));
    }
    @Override
    public boolean isStudent(User user) {
        return user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT));
    }
}
