package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.FeedbackAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, Long> {
    List<FeedbackAnswer> findBySessionFacultyFacultyId(Long facultyId);
}