package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.FeedbackComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackCommentRepository extends JpaRepository<FeedbackComment, Long> {
}