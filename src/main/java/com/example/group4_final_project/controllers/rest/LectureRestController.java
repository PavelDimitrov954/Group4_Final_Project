package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AssignmentHelper;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.models.DTOs.AssignmentDto;
import com.example.group4_final_project.models.DTOs.LectureDto;
import com.example.group4_final_project.models.DTOs.WikiPageDto;
import com.example.group4_final_project.models.filtering.FilterOptionsLecture;
import com.example.group4_final_project.models.models.Assignment;
import com.example.group4_final_project.models.models.Submission;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.AssignmentService;
import com.example.group4_final_project.services.contracts.LectureService;
import com.example.group4_final_project.services.contracts.SubmissionService;
import com.example.group4_final_project.services.contracts.WikiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

    private final LectureService lectureService;
    private final SubmissionService submissionService;
    private final AssignmentService assignmentService;
    private final AuthenticationHelper authenticationHelper;
    private final WikiService wikiService;
    private final AssignmentHelper assignmentHelper;


    @Autowired
    public LectureRestController(LectureService lectureService, SubmissionService submissionService, AuthenticationHelper authenticationHelper, AssignmentService assignmentService, WikiService wikiService, AssignmentHelper assignmentHelper) {
        this.lectureService = lectureService;
        this.submissionService = submissionService;
        this.authenticationHelper = authenticationHelper;
        this.assignmentService = assignmentService;
        this.wikiService = wikiService;
        this.assignmentHelper = assignmentHelper;
    }

    @GetMapping("/search/filter")
    public ResponseEntity<List<LectureDto>> searchLectures(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "title") String sortBy, // Replace with a valid field name
            @RequestParam(required = false, defaultValue = "true") boolean asc) {

        FilterOptionsLecture filterOptions = new FilterOptionsLecture(courseId, title, description, sortBy, asc);
        List<LectureDto> filteredLectures = lectureService.getLecturesByFilter(filterOptions);
        return ResponseEntity.ok(filteredLectures);
    }



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
        try {
            authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(lectureService.getLectureById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<LectureDto>> getAllLectures(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(lectureService.getAllLectures());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectureDto> updateLecture(@RequestHeader HttpHeaders headers,
                                                    @PathVariable Integer id,
                                                    @Valid @RequestBody LectureDto lectureDto) {
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

    @PostMapping("/{lectureId}/assignment")
    public ResponseEntity<AssignmentDto> createAssignment(@RequestHeader HttpHeaders headers,
                                                          @PathVariable Integer lectureId,
                                                          @RequestBody AssignmentDto assignmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(assignmentService.save(lectureId, assignmentDto, user));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{lectureId}/assignment")
    public ResponseEntity<AssignmentDto> updateAssignment(@RequestHeader HttpHeaders headers,
                                                          @PathVariable Integer lectureId,
                                                          @RequestBody AssignmentDto assignmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(assignmentService.update(lectureId, assignmentDto, user));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/{lectureId}/assignment")
    public ResponseEntity<Void> deleteAssignment(@RequestHeader HttpHeaders headers,
                                                 @PathVariable Integer lectureId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            assignmentService.delete(lectureId, user);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/{lectureId}/upload")
    public ResponseEntity<String> uploadAssignment(@RequestHeader HttpHeaders headers,
                                                   @PathVariable Integer lectureId,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            String fileUrl = submissionService.handleAssignmentUpload(lectureId, user.getId(), file);
            return ResponseEntity.ok(fileUrl);
        } catch (EntityNotFoundException | IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }



    @GetMapping("/search")
    public ResponseEntity<List<WikiPageDto>> searchWikipedia(@RequestHeader HttpHeaders headers,
                                                             @RequestParam String searchTerm) {
        try {
            authenticationHelper.tryGetUser(headers);
            List<WikiPageDto> summaries = wikiService.searchWikipedia(searchTerm);
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
