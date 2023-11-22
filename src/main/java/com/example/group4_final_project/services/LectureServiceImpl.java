package com.example.group4_final_project.services;

import com.example.group4_final_project.helpers.LectureMapper;
import com.example.group4_final_project.models.FilterOptionsLecture;
import com.example.group4_final_project.models.Lecture.Lecture;
import com.example.group4_final_project.models.Lecture.LectureDto;
import com.example.group4_final_project.repositories.LectureRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public LectureDto createLecture(LectureDto lectureDto) {
        Lecture lecture = lectureMapper.toEntity(lectureDto);
        return lectureMapper.toDto(lectureRepository.save(lecture));
    }

    @Override
    public LectureDto updateLecture(Integer id, LectureDto lectureDto) {
        Lecture existingLecture = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        existingLecture.setTitle(lectureDto.getTitle());
        existingLecture.setDescription(lectureDto.getDescription());
        existingLecture.setVideoLink(lectureDto.getVideoLink());

        return lectureMapper.toDto(lectureRepository.save(existingLecture));
    }


    @Override
    public void deleteLecture(Integer id) {
        lectureRepository.deleteById(id);
    }

    @Override
    public LectureDto getLectureById(Integer id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));
        return lectureMapper.toDto(lecture);
    }

    @Override
    public List<LectureDto> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(lectureMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions) {
        return lectureRepository.findAll((root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (filterOptions.getCourseId().isPresent()) {
                        predicates.add(cb.equal(root.get("courseId"), filterOptions.getCourseId()));
                    }
                    if (filterOptions.getTitle().isPresent()) {
                        predicates.add(cb.like(root.get("title"), "%" + filterOptions.getTitle() + "%"));
                    }
                    if (filterOptions.getDescription().isPresent()) {
                        predicates.add(cb.like(root.get("description"), "%" + filterOptions.getDescription() + "%"));
                    }
                    return cb.and(predicates.toArray(new Predicate[0]));
                }).stream()
                .map(lectureMapper::toDto)
                .collect(Collectors.toList());
    }

}
