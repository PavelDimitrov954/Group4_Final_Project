package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.models.Enrollment;

import java.util.List;

public interface EnrollmentService {
    Enrollment createEnrollment(Enrollment enrollment);

    Enrollment getEnrollmentById(int id);

    List<Enrollment> getAllEnrollments();

    Enrollment updateEnrollment(int id, Enrollment enrollmentDetails);

    void deleteEnrollment(int id);

    Enrollment enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);
    boolean isStudentEnrolled(int studentId, int courseId);

}
