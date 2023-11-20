package com.example.group4_final_project.services;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.Course;
import com.example.group4_final_project.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course getById(int id) {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Course", id));
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(Course course) {
        getById(course.getId());
        courseRepository.save(course);
        return course;
    }

//TODO This method needs further evaluation and testing before making it as final

    //    @org.springframework.transaction.annotation.Transactional
//    public Course update(Course course) {
//        return courseRepository.findById(course.getCourseId())
//                .map(existingCourse -> {
//                    if (course.getDescription() != null) existingCourse.setDescription(course.getDescription());
//                    if (course.getTitle() != null) existingCourse.setTitle(course.getTitle());
//                    existingCourse.setUpdatedAt(Timestamp.from(Instant.now()));
//                    if (course.getTopic() != null) existingCourse.setTopic(course.getTopic());
//                    if (course.getStatus() != null) existingCourse.setStatus(course.getStatus());
//                    if (course.getEnrollments() != null) existingCourse.setEnrollments(course.getEnrollments());
//                    if (course.getStartDate() != null) existingCourse.setStartDate(course.getStartDate());
//                    return courseRepository.save(existingCourse);
//                })
//                .orElseThrow(() -> new EntityNotFoundException("Course", course.getCourseId()));
//    }
    @Override
    @Transactional
    public Course update(int id, Course courseUpdate) {
        Course existingCourse = getById(id);

        existingCourse.setStatus(courseUpdate.getStatus());
        existingCourse.setEnrollments(courseUpdate.getEnrollments());
        existingCourse.setTopic(courseUpdate.getTopic());
        existingCourse.setDescription(courseUpdate.getDescription());
        existingCourse.setTeacher(courseUpdate.getTeacher());
        existingCourse.setTitle(courseUpdate.getTitle());
        existingCourse.setUpdatedAt(Timestamp.from(Instant.now()));

        return courseRepository.save(existingCourse);

    }

    @Override
    @Transactional
    public void delete(int id) {
        Course course = getById(id);
        courseRepository.delete(course);

    }
}
