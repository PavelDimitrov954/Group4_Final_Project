package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.models.FilterOptionsLecture;
import com.example.group4_final_project.models.Lecture.LectureDto;
import com.example.group4_final_project.services.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureRestController {

    private final LectureService lectureService;

    @Autowired
    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<LectureDto>> searchLectures(@RequestHeader HttpHeaders headers,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description) {

        FilterOptionsLecture filterOptions = new FilterOptionsLecture(courseId, title, description);

        List<LectureDto> filteredLectures = lectureService.getLecturesByFilter(filterOptions);
        return ResponseEntity.ok(filteredLectures);
    }

    @PostMapping
    public ResponseEntity<LectureDto> createLecture(@RequestBody LectureDto lectureDto) {
        return ResponseEntity.ok(lectureService.createLecture(lectureDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureDto> getLectureById(@PathVariable Integer id) {
        return ResponseEntity.ok(lectureService.getLectureById(id));
    }

    @GetMapping
    public ResponseEntity<List<LectureDto>> getAllLectures() {
        return ResponseEntity.ok(lectureService.getAllLectures());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectureDto> updateLecture(@PathVariable Integer id, @RequestBody LectureDto lectureDto) {
        return ResponseEntity.ok(lectureService.updateLecture(id, lectureDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Integer id) {
        lectureService.deleteLecture(id);
        return ResponseEntity.ok().build();
    }
}
