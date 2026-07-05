package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
