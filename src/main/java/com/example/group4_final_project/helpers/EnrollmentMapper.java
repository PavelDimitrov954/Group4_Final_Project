package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.DTOs.EnrollmentDto;
import com.example.group4_final_project.models.models.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class EnrollmentMapper {
    private final UserMapper userMapper;

    @Autowired
    public EnrollmentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public EnrollmentDto toDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setUser(userMapper.fromUser(enrollment.getStudent()));
        dto.setId(enrollment.getId());

        return dto;
    }


    public Set<EnrollmentDto> toDto(Set<Enrollment> enrollmentSet) {
        return new HashSet<>(enrollmentSet.stream().map(this::toDto)
                .toList());
    }
}
