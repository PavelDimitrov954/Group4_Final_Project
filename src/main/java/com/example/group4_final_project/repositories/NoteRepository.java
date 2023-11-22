package com.example.group4_final_project.repositories;

import com.example.group4_final_project.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    // Find notes by student ID
    List<Note> findByStudentId(Integer studentId);

    // Find notes for a specific lecture
    List<Note> findByLectureId(Integer lectureId);

    // Find notes by student for a specific lecture
    List<Note> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);

    // Search notes by content
    List<Note> findByContentContainingIgnoreCase(String content);
}
