package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.FeedbackSession;
import com.feedback.feedback.system.entity.Student;
import com.feedback.feedback.system.entity.Course;
import com.feedback.feedback.system.entity.Section;
import com.feedback.feedback.system.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackSessionRepository extends JpaRepository<FeedbackSession, Long> {
    boolean existsByStudentAndCourseAndSectionAndFaculty(Student student, Course course, Section section, Faculty faculty);
}