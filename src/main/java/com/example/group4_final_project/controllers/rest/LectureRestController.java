package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.models.FilterOptionsLecture;
import com.example.group4_final_project.models.Lecture.LectureDto;
import com.example.group4_final_project.services.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

    private final LectureService lectureService;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public LectureRestController(LectureService lectureService, AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.authenticationHelper = authenticationHelper;
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
    public ResponseEntity<LectureDto> createLecture(@RequestHeader HttpHeaders headers,
                                                    @RequestBody LectureDto lectureDto) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(lectureService.createLecture(lectureDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureDto> getLectureById(@RequestHeader HttpHeaders headers,@PathVariable Integer id) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(lectureService.getLectureById(id));
    }

    @GetMapping
    public ResponseEntity<List<LectureDto>> getAllLectures(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(lectureService.getAllLectures());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectureDto> updateLecture(@RequestHeader HttpHeaders headers,
                                                    @PathVariable Integer id,
                                                    @RequestBody LectureDto lectureDto) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(lectureService.updateLecture(id, lectureDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@RequestHeader HttpHeaders headers,
                                              @PathVariable Integer id) {
        authenticationHelper.tryGetUser(headers);
        lectureService.deleteLecture(id);
        return ResponseEntity.ok().build();
    }
}
