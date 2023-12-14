package com.example.group4_final_project.models.DTOs;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.CourseTopicMapper;
import com.example.group4_final_project.models.models.CourseTopic;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FilterDtoCourse {
    private final CourseTopicMapper courseTopicMapper;
    private final UserRepository userRepository;

    @Nullable
    private String courseTitle;
    @Nullable
    private String courseTopic;
    @Nullable
    private String teacher;
    @Nullable
    private Integer rating;

    @Autowired
    public FilterDtoCourse(CourseTopicMapper courseTopicMapper, UserRepository userRepository) {
        this.courseTopicMapper = courseTopicMapper;

        this.userRepository = userRepository;
    }

    public CourseTopic getCourseTopic() {
        if (StringUtils.hasText(courseTopic)) {
            try {
                return courseTopicMapper.toEntity(new CourseTopicDto(courseTopic));
            } catch (EntityNotFoundException e) {
                // Handle the exception (log, throw a custom exception, etc.)
                // For now, let's print the stack trace for debugging purposes
                e.printStackTrace();
            }
        }
        return null; // Return null if courseTopic is empty or null
    }

    public User getTeacher() {

        if (StringUtils.hasText(teacher)) {

            return userRepository.findByFirstName(teacher);
        } else {
            return null; // Handle the case where teacherName is empty or null
        }
    }

    public void setCourseTopic(@Nullable String courseTopic) {
        this.courseTopic = courseTopic;
    }

    public void setTeacher(@Nullable String teacher) {
        this.teacher = teacher;
    }

    @Nullable
    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(@Nullable String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Nullable
    public Integer getRating() {
        return rating;
    }

    public void setRating(@Nullable Integer rating) {
        this.rating = rating;
    }
}
