package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.User;

import java.util.List;

public interface LectureService {
    LectureDto createLecture(LectureDto lectureDto, User creator);

    LectureDto updateLecture(Integer id, LectureDto lectureDto, User user);

    void deleteLecture(Integer id, User user);

    LectureDto getLectureById(Integer id);
    List<LectureDto> getLecturesByCourseId(Integer courseId);

    List<LectureDto> getAllLectures();

    List<LectureDto> getLecturesByFilter(FilterOptionsLecture filterOptions);

    Assignment getAssignmentByLectureId(Integer lectureId);
}
