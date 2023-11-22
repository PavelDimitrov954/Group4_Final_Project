package com.example.group4_final_project.helpers;

import com.example.group4_final_project.models.Lecture.Lecture;
import com.example.group4_final_project.models.Lecture.LectureDto;
import org.springframework.stereotype.Service;

@Service
public class LectureMapper {

    public LectureDto toDto(Lecture lecture) {
        LectureDto dto = new LectureDto();
        dto.setTitle(lecture.getTitle());
        dto.setDescription(lecture.getDescription());
        dto.setVideoLink(lecture.getVideoLink());
        return dto;
    }

    public Lecture toEntity(LectureDto dto) {
        Lecture lecture = new Lecture();
        lecture.setTitle(dto.getTitle());
        lecture.setDescription(dto.getDescription());
        lecture.setVideoLink(dto.getVideoLink());
        return lecture;
    }
}
