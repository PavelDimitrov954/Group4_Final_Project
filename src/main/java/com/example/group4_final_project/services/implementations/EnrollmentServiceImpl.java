package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.models.models.Course;
import com.example.group4_final_project.models.models.Enrollment;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.CourseRepository;
import com.example.group4_final_project.repositories.EnrollmentRepository;
import com.example.group4_final_project.repositories.UserRepository;
import com.example.group4_final_project.services.contracts.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 UserRepository userRepository,
                                 CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public boolean isStudentEnrolled(int studentId, int courseId){
       return findByStudentIdAndCourseId(studentId, courseId).isPresent();
    }
    public Optional<Enrollment> findByStudentIdAndCourseId(int studentId, int courseId){
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    @Override
    @Transactional
    public Enrollment createEnrollment(Enrollment enrollment) {
        //validating student and user:
        userRepository.findById(enrollment.getStudent().getId()).orElseThrow(() -> new EntityNotFoundException("Student", enrollment.getStudent().getId()));
        courseRepository.findById(enrollment.getCourse().getId()).orElseThrow(() -> new EntityNotFoundException("Course", enrollment.getCourse().getId()));

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment", id));
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    @Transactional
    public Enrollment updateEnrollment(int id, Enrollment enrollmentDetails) {
        Enrollment existingEnrollment = getEnrollmentById(id);

        if (enrollmentDetails.getCourse() != null) {
            existingEnrollment.setCourse(enrollmentDetails.getCourse());
        }
        if (enrollmentDetails.getStudent() != null) {
            existingEnrollment.setStudent(enrollmentDetails.getStudent());
        }
        return enrollmentRepository.save(existingEnrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(int id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollmentRepository.delete(enrollment);
    }

    @Override
    @Transactional
    public Enrollment enrollStudentInCourse(int studentId, int courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(Timestamp.from(Instant.now()));

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void removeStudentFromCourse(int studentId, int courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment",
                        String.format("student id: %d", studentId),
                        String.format("and course id: %d", courseId)));
        enrollmentRepository.delete(enrollment);
    }

}
