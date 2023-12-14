package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.exceptions.UnauthorizedOperationException;
import com.example.group4_final_project.helpers.AssignmentHelper;
import com.example.group4_final_project.helpers.SubmissionMapper;
import com.example.group4_final_project.models.DTOs.SubmissionDto;
import com.example.group4_final_project.models.DTOs.SubmissionUpdateDto;
import com.example.group4_final_project.models.enums.RoleName;
import com.example.group4_final_project.models.models.*;
import com.example.group4_final_project.repositories.*;
import com.example.group4_final_project.services.contracts.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    public static final String INVALID_AUTHORIZATION = "Invalid authorization";
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AssignmentHelper assignmentHelper;
    private final SubmissionMapper submissionMapper;
    private final RoleRepository roleRepository;

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository, UserRepository userRepository, AssignmentHelper assignmentHelper, SubmissionMapper submissionMapper, RoleRepository roleRepository, CourseRepository courseRepository, LectureRepository lectureRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.assignmentHelper = assignmentHelper;
        this.submissionMapper = submissionMapper;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
    }

    public Submission save(Submission submission) {
        return submissionRepository.save(submission);
    }

    public String handleAssignmentUpload(Integer lectureId, Integer userId, MultipartFile file) throws IOException, EntityNotFoundException {
        Assignment assignment = assignmentRepository.findByLectureId(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment for lecture", lectureId));

        String fileUrl = assignmentHelper.uploadAssignment(file);

        // Check for an existing submission
        Optional<Submission> existingSubmission = submissionRepository.findByAssignmentIdAndUserId(assignment.getId(), userId);

        Submission submission;
        if (existingSubmission.isPresent()) {
            // Update existing submission
            submission = existingSubmission.get();
            submission.setSubmissionURL(fileUrl);
            submission.setSubmittedAt(Instant.now());
        } else {
            // Create new submission
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            submission = new Submission();
            submission.setUser(user);
            submission.setAssignment(assignment);
            submission.setSubmissionURL(fileUrl);
            submission.setSubmittedAt(Instant.now());
        }

        save(submission);
        return fileUrl;
    }

    @Override
    public void update(int id, User user, SubmissionUpdateDto submissionUpdateDto) {
        if(!user.getRoles().contains(roleRepository.findByRoleName(RoleName.TEACHER))
                && !user.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))){
            throw new AuthorizationException(INVALID_AUTHORIZATION);
        }

        Submission submission = submissionMapper.fromDto(id,submissionUpdateDto);

        save(submission);

    }


    public List<SubmissionDto> getStudentSubmission(User loginUser) {
        List<Course> courses;
        if(loginUser.getRoles().contains(roleRepository.findByRoleName(RoleName.ADMIN))){
            courses= courseRepository.findAll();
        }
        else{
            courses = courseRepository.findAllByTeacher(loginUser).stream().toList();
        }
         List<SubmissionDto>  submissionDtoList = new ArrayList<>();
        courses.stream().forEach(course -> {
            SubmissionDto submissionDto = new SubmissionDto();
            List <Submission> submissions =submissionRepository.findAll().stream()
                 .filter(submission -> submission.getAssignment().getLecture().getCourse().equals(course)
                 && submission.getGrade()==null).toList();

            if(!submissions.isEmpty()){
                submissionDto.setCourse(course);
                submissionDto.setSubmission(submissions);
                submissionDtoList.add(submissionDto);
            }

        });



        return submissionDtoList;


    }

}
