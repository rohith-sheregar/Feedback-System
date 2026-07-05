package com.feedback.feedback.system.service;

import com.feedback.feedback.system.entity.*;
import com.feedback.feedback.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private CourseAssignmentRepository courseAssignmentRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private FeedbackSessionRepository feedbackSessionRepo;

    @Autowired
    private FeedbackAnswerRepository feedbackAnswerRepo;

    @Autowired
    private FeedbackCommentRepository feedbackCommentRepo;

    public List<CourseAssignment> getStudentCourseAssignments(Student student) {
        try {
            if (student == null) {
                System.out.println("Warning: Null student passed to getStudentCourseAssignments");
                return List.of();
            }

            if (student.getSection() == null) {
                System.out.println("Warning: Student " + student.getUsn() + " has no section assigned");
                return List.of();
            }

            List<CourseAssignment> assignments = courseAssignmentRepo.findBySectionSectionId(student.getSection().getSectionId());
            if (assignments.isEmpty()) {
                System.out.println("No course assignments found for section: " + student.getSection().getSectionId());
            }
            return assignments;
        } catch (Exception e) {
            System.err.println("Error fetching course assignments: " + e.getMessage());
            return List.of();
        }
    }

    public List<Question> getFeedbackQuestions() {
        return questionRepo.findAll();
    }

    public boolean hasFeedback(Student student, CourseAssignment courseAssignment) {
        if (student == null || courseAssignment == null || courseAssignment.getCourse() == null ||
                courseAssignment.getSection() == null || courseAssignment.getFaculty() == null) {
            return false;
        }

        // Check if there's any feedback session for this student and course combination
        return feedbackSessionRepo.findAll().stream()
            .anyMatch(session ->
                session.getStudent() != null &&
                session.getStudent().getStudentId().equals(student.getStudentId()) &&
                session.getCourse() != null &&
                session.getCourse().getCourseId().equals(courseAssignment.getCourse().getCourseId()) &&
                session.getSection() != null &&
                session.getSection().getSectionId().equals(courseAssignment.getSection().getSectionId()) &&
                session.getFaculty() != null &&
                session.getFaculty().getFacultyId().equals(courseAssignment.getFaculty().getFacultyId())
            );
    }

    public void saveFeedback(Student student, Long courseAssignmentId, List<Integer> ratings, String voiceFeedback) {
        Optional<CourseAssignment> courseAssignmentOpt = courseAssignmentRepo.findById(courseAssignmentId);
        if (courseAssignmentOpt.isEmpty()) {
            throw new RuntimeException("Course assignment not found");
        }
        CourseAssignment courseAssignment = courseAssignmentOpt.get();

        // Check if feedback already exists
        if (hasFeedback(student, courseAssignment)) {
            throw new RuntimeException("Feedback already submitted for this course");
        }

        // Create FeedbackSession
        FeedbackSession session = new FeedbackSession();
        session.setStudent(student);
        session.setCourse(courseAssignment.getCourse());
        session.setSection(courseAssignment.getSection());
        session.setFaculty(courseAssignment.getFaculty());
        session.setCreatedAt(LocalDateTime.now());
        session = feedbackSessionRepo.save(session); // Save and get the persisted session

        // Save ratings (FeedbackAnswer)
        List<Question> questions = getFeedbackQuestions();
        if (ratings != null && !ratings.isEmpty()) {
            for (int i = 0; i < Math.min(ratings.size(), questions.size()); i++) {
                if (ratings.get(i) != null && ratings.get(i) >= 1 && ratings.get(i) <= 5) {
                    FeedbackAnswer answer = new FeedbackAnswer();
                    answer.setSession(session);
                    answer.setQuestion(questions.get(i));
                    answer.setRating(ratings.get(i));
                    answer.setAnswerText(""); // Set empty text for rating-only answers
                    feedbackAnswerRepo.save(answer);
                }
            }
        }

        // Save voice feedback (FeedbackComment)
        if (voiceFeedback != null && !voiceFeedback.trim().isEmpty()) {
            FeedbackComment comment = new FeedbackComment();
            comment.setSession(session);
            comment.setRawText(voiceFeedback.trim());
            feedbackCommentRepo.save(comment);
        }
    }

    public long countFeedbacksForCourseAssignment(CourseAssignment courseAssignment) {
        return feedbackAnswerRepo.findAll().stream()
            .filter(answer -> answer.getSession() != null &&
                    answer.getSession().getCourse() != null &&
                    answer.getSession().getCourse().getCourseId().equals(courseAssignment.getCourse().getCourseId()) &&
                    answer.getSession().getSection() != null &&
                    answer.getSession().getSection().getSectionId().equals(courseAssignment.getSection().getSectionId()) &&
                    answer.getSession().getFaculty() != null &&
                    answer.getSession().getFaculty().getFacultyId().equals(courseAssignment.getFaculty().getFacultyId()))
            .map(answer -> answer.getSession().getStudent().getStudentId())
            .distinct()
            .count();
    }

    public Map<Question, Map<Integer, Long>> getQuestionStatistics(CourseAssignment courseAssignment) {
        List<FeedbackAnswer> answers = feedbackAnswerRepo.findAll().stream()
            .filter(answer -> answer.getSession() != null &&
                    answer.getSession().getCourse() != null &&
                    answer.getSession().getCourse().getCourseId().equals(courseAssignment.getCourse().getCourseId()) &&
                    answer.getSession().getSection() != null &&
                    answer.getSession().getSection().getSectionId().equals(courseAssignment.getSection().getSectionId()) &&
                    answer.getSession().getFaculty() != null &&
                    answer.getSession().getFaculty().getFacultyId().equals(courseAssignment.getFaculty().getFacultyId()))
            .collect(Collectors.toList());

        Map<Question, Map<Integer, Long>> stats = new HashMap<>();

        for (FeedbackAnswer answer : answers) {
            Question question = answer.getQuestion();
            if (question != null) {
                stats.putIfAbsent(question, new HashMap<>());
                Map<Integer, Long> ratingsCount = stats.get(question);
                int rating = answer.getRating();
                ratingsCount.put(rating, ratingsCount.getOrDefault(rating, 0L) + 1);
            }
        }

        return stats;
    }

    public List<String> getFeedbackComments(CourseAssignment courseAssignment) {
        return feedbackAnswerRepo.findAll().stream()
            .filter(answer -> answer.getSession() != null &&
                    answer.getSession().getCourse() != null &&
                    answer.getSession().getCourse().getCourseId().equals(courseAssignment.getCourse().getCourseId()) &&
                    answer.getSession().getSection() != null &&
                    answer.getSession().getSection().getSectionId().equals(courseAssignment.getSection().getSectionId()) &&
                    answer.getSession().getFaculty() != null &&
                    answer.getSession().getFaculty().getFacultyId().equals(courseAssignment.getFaculty().getFacultyId()) &&
                    answer.getAnswerText() != null &&
                    !answer.getAnswerText().trim().isEmpty())
            .map(FeedbackAnswer::getAnswerText)
            .collect(Collectors.toList());
    }
}
