package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.CourseMapper;
import com.example.group4_final_project.models.DTOs.CourseDtoView;
import com.example.group4_final_project.models.DTOs.CourseDto;
import com.example.group4_final_project.models.DTOs.UpdateCourseDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsCourse;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.CourseRepository;
import com.example.group4_final_project.repositories.CourseTopicRepository;
import com.example.group4_final_project.repositories.RoleRepository;
import com.example.group4_final_project.services.contracts.CourseService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    //    The user must be able to filter the catalog by course name, course topic, teacher, and rating, as well as to sort the catalog by name and/or rating.
    private static final String TEACHER_ROLE = "teacher";
    private static final String ADMIN_ROLE = "admin";
    private static final String UNAUTHORIZED_USER_EXCEPTION = "You are unauthorized to do this action";
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final CourseTopicRepository courseTopicRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper,
                             CourseTopicRepository courseTopicRepository,
                             RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseTopicRepository = courseTopicRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public CourseDtoView getById(int id) {
        return courseMapper.toDtoView(courseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Course", id)));
    }

    @Override
    public List<CourseDtoView> getAllCourses() {
        return courseMapper.toDto(courseRepository.findAll());
    }

    @Override
    public List<CourseDtoView> getAllCourses(FilterOptionsCourse filterOptionsCourse) {
        return courseMapper.toDto(courseRepository.findAll((Specification<Course>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filterOptionsCourse.getCourseTitle().ifPresent(title ->
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%")));

            filterOptionsCourse.getCourseTopic().ifPresent(topic ->
                    predicates.add(criteriaBuilder.equal(root.get("topic"), topic)));

            filterOptionsCourse.getTeacher().ifPresent(teacher ->
                    predicates.add(criteriaBuilder.equal(root.get("teacher"), teacher)));
            //TODO Has a potential to break; criteriaBuilder might need to be used with .like
            filterOptionsCourse.getRating().ifPresent(rating ->
                    predicates.add(criteriaBuilder.equal(root.get("rating"), rating)));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));

    }

    @Override
    public CourseDtoView createCourse(User userWhoCreates, CourseDto courseDto) {
        if (!userWhoCreates.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER))) {
            throw new AuthorizationException(UNAUTHORIZED_USER_EXCEPTION);
        }
        Course course = courseMapper.toEntity(courseDto);
        course.setTeacher(userWhoCreates);
        //TODO Validation for courseTopic
        CourseTopic topic = courseTopicRepository.findByName(courseDto.getTopic()).get();
        course.setTopic(topic);
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
        if (courseDto.getStartDate() != null){
            courseToUpdate.setStartDate(courseDto.getStartDate());
        }
        return courseMapper.toDtoView( courseRepository.save(courseToUpdate));
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

    //It is not in the mappers package because of the use of courseTopicRepository!
//    public Course mapUpdateToEntity(UpdateCourseDto dto){
//        Course course = new Course();
//        course.setTopic(courseTopicRepository.findByName(dto.getTopic()).get());
//        course.setDescription(dto.getDescription());
//        course.setTitle(dto.getTitle());
//        course.setStartDate(dto.getStartDate());
//        course.setUpdatedAt(Timestamp.from(Instant.now()));
//        return course;
//
//    }
}
