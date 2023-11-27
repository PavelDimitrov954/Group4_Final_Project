package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.DTOs.LectureDto;

import java.util.List;

public interface LectureService {
    LectureDto createLecture(LectureDto lectureDto);
    LectureDto updateLecture(Integer id, LectureDto lectureDto);
    void deleteLecture(Integer id);
    LectureDto getLectureById(Integer id);
    List<LectureDto> getAllLectures();
    List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions);
}
