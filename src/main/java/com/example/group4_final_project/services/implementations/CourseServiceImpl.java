package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.CourseMapper;

import com.example.group4_final_project.helpers.CourseTopicMapper;
import com.example.group4_final_project.models.DTOs.*;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.Enrollment;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.*;
import com.example.group4_final_project.services.contracts.CourseService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    //    The user must be able to filter the catalog by course name, course topic, teacher, and rating, as well as to sort the catalog by name and/or rating.
    private static final String TEACHER_ROLE = "teacher";
    private static final String ADMIN_ROLE = "admin";
    private static final String UNAUTHORIZED_USER_EXCEPTION = "You are unauthorized to do this action";
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseTopicMapper courseTopicMapper;
    private final CourseTopicRepository courseTopicRepository;

    private final RoleRepository roleRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper,
                             CourseTopicMapper courseTopicMapper, CourseTopicRepository courseTopicRepository,
                             RoleRepository roleRepository, EnrollmentRepository enrollmentRepository, LectureRepository lectureRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseTopicMapper = courseTopicMapper;
        this.courseTopicRepository = courseTopicRepository;
        this.roleRepository = roleRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public CourseDtoView getById(int id) {
        return courseMapper.toDtoView(courseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Course", id)));
    }

    @Override
    public List<CourseDtoView> getAllCourses() {
        return courseMapper.toDtoViews(courseRepository.findAll());
    }

    @Override
    public List<CourseDtoView> getAllCourses(FilterOptionsCourse filterOptionsCourse) {
        return courseMapper.toDtoViews(courseRepository.findAll((Specification<Course>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filterOptionsCourse.getCourseTitle().ifPresent(courseTitle ->
                    predicates.add(criteriaBuilder.like(root.get("courseTitle"), "%" + courseTitle + "%")));

            filterOptionsCourse.getCourseTopic().ifPresent(courseTopic ->
                    predicates.add(criteriaBuilder.equal(root.get("courseTopic"), courseTopic)));

            filterOptionsCourse.getTeacher().ifPresent(teacher ->
                    predicates.add(criteriaBuilder.equal(root.get("teacher"), teacher)));
            //TODO Has a potential to break; criteriaBuilder might need to be used with .like
            filterOptionsCourse.getRating().ifPresent(rating ->
                    predicates.add(criteriaBuilder.equal(root.get("rating"), rating)));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));

    }

    @Override
    public List<CourseDto> getAllCoursesBYUser(User user) {
        List<CourseDto> courseDto = new ArrayList<>();
        if (user.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER)) ||
                user.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))) {
            courseDto =   courseRepository
                    .findAllByTeacher(user).stream().map(courseMapper::toDto).toList();
        } else if (user.getRoles().contains(roleRepository.findByRoleName(RoleName.STUDENT))) {
            Optional<Enrollment> enrollments = enrollmentRepository.findEnrollmentByStudent(user);
            courseDto =  enrollments.stream().map(e -> courseMapper.toDto(e.getCourse())).toList();
        }

        courseDto.stream().forEach(course ->{
            course.setLectures(lectureRepository.findByCourseId(course.getCourseId()).get());
        });
        return courseDto;
    }


    @Override
    public CourseDtoView createCourse(User userWhoCreates, CreateCourseDto courseDto) {
        if (!userWhoCreates.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER))) {
            throw new AuthorizationException(UNAUTHORIZED_USER_EXCEPTION);
        }
        Course course = courseMapper.toEntity(courseDto);
        course.setTeacher(userWhoCreates);
        //TODO Validation for courseTopic
        CourseTopic topic = courseTopicRepository.findByName(courseDto.getTopic()).get();
        course.setTopic(topic);
        try {
            Timestamp startDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(courseDto.getStartDate()).getTime());
            course.setStartDate(startDate);
        } catch (ParseException e) {
            System.out.println("****There was a parsing error again!****");
        }

        courseRepository.save(course);
        return courseMapper.toDtoView(course);
    }

    //TODO This method needs further evaluation and testing before making it as final
    @Override
    @Transactional
    public CourseDtoView updateCourse(User userWhoUpdates, int courseId, UpdateCourseDto courseDto) {
        Course courseToUpdate = courseRepository.findById(courseId).
                orElseThrow(() -> new EntityNotFoundException("Course", courseId));
        if (!courseToUpdate.getTeacher().equals(userWhoUpdates)) {
            throw new AuthorizationException(UNAUTHORIZED_USER_EXCEPTION);
        }
        if (courseDto.getDescription() != null) {
            courseToUpdate.setDescription(courseDto.getDescription());
        }
        if (courseDto.getTitle() != null) {
            courseToUpdate.setTitle(courseDto.getTitle());
        }
        courseToUpdate.setUpdatedAt(Timestamp.from(Instant.now()));
        if (courseDto.getTopic() != null) {
            CourseTopic topic = courseTopicRepository.findByName(courseDto.getTopic()).get();
            courseToUpdate.setTopic(topic);

        }
        if (courseDto.getStartDate() != null) {
            try {
                Timestamp startDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(courseDto.getStartDate()).getTime());
                courseToUpdate.setStartDate(startDate);
            } catch (ParseException e) {
                System.out.println("****There was a parsing error again!****");
            }
        }
        return courseMapper.toDtoView(courseRepository.save(courseToUpdate));
    }

    @Override
    @Transactional
    public void delete(User userWhoDeletes, int id) {
        Course courseToDelete = courseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Course", id));
        if ((!courseToDelete.getTeacher().equals(userWhoDeletes)) ||
                userWhoDeletes.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))) {
            throw new AuthorizationException(UNAUTHORIZED_USER_EXCEPTION);
        }
        courseRepository.delete(courseToDelete);

    }

    @Override
    public List<CourseDtoView> getTopCoursesByRating(int limit) {
        return courseMapper.toDtoViews(courseRepository.findTopCoursesByRating(limit));
    };

    @Override
    public List<CourseTopicDto> getAllCourseTopics(){
        return courseTopicMapper.toDto(courseTopicRepository.findAll());
    }
}
