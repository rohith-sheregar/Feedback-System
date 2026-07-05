package com.feedback.feedback.system.controller;

import com.feedback.feedback.system.entity.Student;
import com.feedback.feedback.system.entity.CourseAssignment;
import com.feedback.feedback.system.entity.Question;
import com.feedback.feedback.system.repository.StudentRepository;
import com.feedback.feedback.system.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")  // Add base mapping
public class StudentController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StudentController.class);

    @Autowired  // Add @Autowired annotation
    private StudentRepository studentRepo;

    @Autowired  // Add @Autowired annotation
    private FeedbackService feedbackService;

    @GetMapping("/dashboard")
    public String showDashboard(Authentication auth, Model model) {
        try {
            log.debug("Starting dashboard load for authentication: {}", auth);

            if (auth == null) {
                log.error("Authentication object is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed. Please login again.");
            }

            String usn = auth.getName();
            log.debug("Loading dashboard for student USN: {}", usn);

            // Find student and handle null case
            Student student = studentRepo.findByUsn(usn).orElseThrow(() -> {
                log.error("No student found with USN: {}", usn);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student account not found. Please contact administrator.");
            });

            // Log student details for debugging
            log.debug("Found student: {} (USN: {})", student.getName(), student.getUsn());
            log.debug("Student section: {}", student.getSection() != null ? student.getSection().getSectionName() : "No section");

            // Check section assignment
            if (student.getSection() == null) {
                log.error("Student {} has no section assigned", usn);
                model.addAttribute("error", "Your account needs a section assignment. Please contact your administrator.");
                return "error";
            }

            // Load course assignments with detailed logging
            List<CourseAssignment> assignments = null;
            try {
                assignments = feedbackService.getStudentCourseAssignments(student);
                log.debug("Found {} course assignments for student {}", assignments.size(), usn);

                if (assignments.isEmpty()) {
                    log.info("No course assignments found for student {} in section {}",
                            usn, student.getSection().getSectionName());
                    model.addAttribute("warning", "No courses have been assigned to your section yet. Please check back later.");
                }
            } catch (Exception e) {
                log.error("Error loading course assignments: {}", e.getMessage(), e);
                assignments = List.of();
            }

            // Load questions with error handling
            List<Question> questions = null;
            try {
                questions = feedbackService.getFeedbackQuestions();
                log.debug("Found {} feedback questions", questions.size());

                if (questions.isEmpty()) {
                    log.warn("No feedback questions found in the system");
                    model.addAttribute("warning", "No feedback questions are currently available.");
                }
            } catch (Exception e) {
                log.error("Error loading feedback questions: {}", e.getMessage(), e);
                questions = List.of();
            }

            // Add everything to model
            model.addAttribute("student", student);
            model.addAttribute("courseAssignments", assignments);
            model.addAttribute("questions", questions);
            model.addAttribute("feedbackService", feedbackService);

            log.debug("Successfully prepared dashboard model for student: {}", usn);
            return "student/dashboard";

        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            log.error("Unexpected error in student dashboard: {}", e.getMessage(), e);
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            model.addAttribute("trace", e.getStackTrace());
            return "error";
        }
    }

    @PostMapping("/submit-feedback")
    public String processFeedback(
            @RequestParam("courseAssignmentId") Long courseAssignmentId,
            @RequestParam Map<String, String> allParams,
            @RequestParam(value = "voiceFeedback", required = false) String voiceFeedback,
            Authentication auth,
            Model model) {
        try {
            String usn = auth.getName();
            Student student = studentRepo.findByUsn(usn)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

            // Extract ratings from form parameters
            List<Integer> ratings = new ArrayList<>();
            int questionCount = feedbackService.getFeedbackQuestions().size();
            for (int i = 0; i < questionCount; i++) {
                String rating = allParams.get("answer[" + i + "]");
                if (rating != null) {
                    ratings.add(Integer.parseInt(rating));
                }
            }

            feedbackService.saveFeedback(student, courseAssignmentId, ratings, voiceFeedback);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error submitting feedback: " + e.getMessage());
        }
        // Re-populate model attributes for the dashboard
        model.addAttribute("student", studentRepo.findByUsn(auth.getName()).orElse(null));
        model.addAttribute("courseAssignments", feedbackService.getStudentCourseAssignments(studentRepo.findByUsn(auth.getName()).orElse(null)));
        model.addAttribute("questions", feedbackService.getFeedbackQuestions());
        model.addAttribute("feedbackService", feedbackService);
        return "student/dashboard";
    }
}
