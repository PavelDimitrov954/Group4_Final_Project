package com.example.group4_final_project.services;

import com.example.group4_final_project.models.FilterOptionsLecture;
import com.example.group4_final_project.models.Lecture.LectureDto;

import java.util.List;

public interface LectureService {
    LectureDto createLecture(LectureDto lectureDto);
    LectureDto updateLecture(Integer id, LectureDto lectureDto);
    void deleteLecture(Integer id);
    LectureDto getLectureById(Integer id);
    List<LectureDto> getAllLectures();
    List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions);
}
