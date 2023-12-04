package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AssignmentMapper;
import com.example.group4_final_project.helpers.LectureMapper;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.Role;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.LectureRepository;
import com.example.group4_final_project.services.contracts.LectureService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final AssignmentMapper assignmentMapper;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper lectureMapper, AssignmentMapper assignmentMapper) {
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    @Transactional
    public LectureDto createLecture(LectureDto lectureDto, User creator) throws AuthorizationException {
        checkUserRoles(creator, List.of(RoleName.TEACHER, RoleName.ADMIN));

        Lecture lecture = lectureMapper.toEntity(lectureDto);
        if (lectureDto.getAssignment() != null) {
            Assignment assignment = assignmentMapper.toEntity(lectureDto.getAssignment());
            assignment.setLecture(lecture); // Set the lecture reference
            lecture.setAssignment(assignment);
        }
        return lectureMapper.toDto(lectureRepository.save(lecture));
    }
//    @Transactional
//public LectureDto createLecture(LectureDto lectureDto) {
//    Lecture lecture = convertToEntity(lectureDto);
//    if (lectureDto.getAssignment() != null) {
//        Assignment assignment = convertToEntity(lectureDto.getAssignment());
//        assignment.setLecture(lecture); // Set the lecture reference
//        lecture.setAssignment(assignment);
//    }
//    Lecture savedLecture = lectureRepository.save(lecture);
//    return convertToDto(savedLecture);
//}


    @Override
    @Transactional
    public LectureDto updateLecture(Integer id, LectureDto lectureDto, User user)
            throws AuthorizationException, EntityNotFoundException {
        Lecture existingLecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", id));
        if (!isUserAuthorizedToModifyLecture(user, existingLecture))
            throw new AuthorizationException("User is not creator or admin !");

        checkUserRoles(user, List.of(RoleName.TEACHER, RoleName.ADMIN));

        // Update basic lecture properties
        existingLecture.setTitle(lectureDto.getTitle());
        existingLecture.setDescription(lectureDto.getDescription());
        existingLecture.setVideoLink(lectureDto.getVideoLink());

        return lectureMapper.toDto(lectureRepository.save(existingLecture));
    }


    @Override
    @Transactional
    public void deleteLecture(Integer id, User user) {
        lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", id));

        checkUserRoles(user, List.of(RoleName.TEACHER, RoleName.ADMIN));


        lectureRepository.deleteById(id);
    }

    @Override
    public LectureDto getLectureById(Integer id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", id));
        return lectureMapper.toDto(lecture);
    }

    @Override
    public List<LectureDto> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(lectureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions) {
        return lectureRepository.findAll((root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    filterOptions.getTitle().ifPresent(title ->
                            predicates.add(cb.like(root.get("title"), "%" + title + "%")));

                    filterOptions.getDescription().ifPresent(description ->
                            predicates.add(cb.like(root.get("description"), "%" + description + "%")));

                    filterOptions.getCourseId().ifPresent(courseId ->
                            predicates.add(cb.equal(root.get("course").get("id"), courseId)));

                    query.orderBy(/* add your Sort criteria here */);

                    return cb.and(predicates.toArray(new Predicate[0]));
                }).stream()
                .map(lectureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Assignment getAssignmentByLectureId(Integer lectureId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);
        if (lecture.isPresent()) {
            return lecture.get().getAssignment();
        } else {
            throw new EntityNotFoundException("Lecture", lectureId);
        }
    }


    private void checkUserRoles(User user, List<RoleName> requiredRoles) {
        boolean hasAnyRole = requiredRoles.stream()
                .anyMatch(requiredRole -> user.getRoles().stream()
                        .anyMatch(userRole -> userRole.getRoleName().equals(requiredRole)));

        if (!hasAnyRole) {
            throw new AuthorizationException("User does not have any of the required roles: " + requiredRoles);
        }
    }


    private boolean isUserAuthorizedToModifyLecture(User user, Lecture lecture) {

        // Check if the user is the teacher of the course
        if (user.equals(lecture.getCourse().getTeacher())) {
            return true;
        }

        // Check if the user has the ADMIN role
        for (Role role : user.getRoles()) {
            if (role.getRoleName() == RoleName.ADMIN) {
                return true;
            }
        }

        // User is not authorized
        return false;
    }
}
