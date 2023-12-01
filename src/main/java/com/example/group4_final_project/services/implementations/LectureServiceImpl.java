package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.LectureMapper;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.models.Lecture;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.repositories.LectureRepository;
import com.example.group4_final_project.services.contracts.LectureService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper lectureMapper) {
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
    }

    @Override
    @Transactional
    public LectureDto createLecture(LectureDto lectureDto, User creator) throws AuthorizationException {

        checkUserRoles(creator, List.of(RoleName.TEACHER, RoleName.ADMIN));

        Lecture lecture = lectureMapper.toEntity(lectureDto);
        return lectureMapper.toDto(lectureRepository.save(lecture));
    }

    @Override
    @Transactional
    public LectureDto updateLecture(Integer id, LectureDto lectureDto, User user) throws AuthorizationException, EntityNotFoundException {
        Lecture existingLecture = lectureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lecture", id));
        if (!isUserAuthorizedToModifyLecture(user, existingLecture))
            throw new AuthorizationException("User is not creator or admin !");


        checkUserRoles(user, List.of(RoleName.TEACHER, RoleName.ADMIN));

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

    // a method that returns a list of lectures based on the filter options passed as a parameter
    @Override
    public List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions) {
        return lectureRepository.findAll((root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (filterOptions.getTitle().isPresent()) {
                        predicates.add(cb.like(root.get("title"), "%" + filterOptions.getTitle() + "%"));
                    }

                    if (filterOptions.getDescription().isPresent()) {
                        predicates.add(cb.like(root.get("description"), "%" + filterOptions.getDescription() + "%"));
                    }

                    if (filterOptions.getCourseId().isPresent()) {
                        predicates.add(cb.equal(root.get("course").get("id"), filterOptions.getCourseId()));
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                }).stream()
                .map(lectureMapper::toDto)
                .collect(Collectors.toList());
    }

    private void checkUserRoles(User user, List<RoleName> requiredRoles) {
        boolean hasAnyRole = requiredRoles.stream()
                .anyMatch(requiredRole -> user.getRoles().stream()
                        .anyMatch(userRole -> userRole.getRoleName() == requiredRole));

        if (!hasAnyRole) {
            throw new AuthorizationException("User does not have any of the required roles: " + requiredRoles);
        }
    }

    private boolean isUserAuthorizedToModifyLecture(User user, Lecture lecture) {
        return user.equals(lecture.getCourse().getTeacher()) || user.getRoles().contains(RoleName.ADMIN);
    }
}
