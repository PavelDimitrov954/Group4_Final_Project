package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.services.contracts.LectureService;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

    private final LectureService lectureService;
    private final SubmissionService submissionService;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public LectureRestController(LectureService lectureService, SubmissionService submissionService, AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.submissionService = submissionService;
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

//    @PostMapping
//    public ResponseEntity<LectureDto> createLecture(@RequestHeader HttpHeaders headers,
//                                                    @RequestBody LectureDto lectureDto) {
//        User user = authenticationHelper.tryGetUser(headers);
//        authenticationHelper.checkUserRole(user, "TEACHER");
//        return ResponseEntity.ok(lectureService.createLecture(lectureDto));
//    }

    @PostMapping
    public ResponseEntity<LectureDto> createLecture(@RequestHeader HttpHeaders headers,
                                                    @Valid @RequestBody LectureDto lectureDto) {
        try {
            User creator = authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(lectureService.createLecture(lectureDto, creator));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureDto> getLectureById(@RequestHeader HttpHeaders headers, @PathVariable Integer id) {
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
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(lectureService.updateLecture(id, lectureDto, user));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@RequestHeader HttpHeaders headers,
                                              @PathVariable Integer id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.deleteLecture(id, user);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/{lectureId}/submitAssignment")
    public ResponseEntity<String> submitAssignment(@PathVariable Integer lectureId,
                                                   @RequestParam("userId") Integer userId,
                                                   @RequestParam("file") MultipartFile file) throws IOException {
        /*try {
            if (file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
            }

            // Additional validations can be added here

            submissionService.submitAssignment(lectureId, userId, file);
            return ResponseEntity.ok("Assignment '" + .getAssignmentTitle() + "' submitted successfully.");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing file");
        }*/
        return null;
    }


}
