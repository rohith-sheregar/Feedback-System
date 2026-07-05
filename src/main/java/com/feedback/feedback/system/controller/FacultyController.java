package com.feedback.feedback.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import com.feedback.feedback.system.repository.FeedbackAnswerRepository;
import com.feedback.feedback.system.repository.FacultyRepository;
import com.feedback.feedback.system.repository.CourseAssignmentRepository;
import com.feedback.feedback.system.entity.Faculty;
import com.feedback.feedback.system.entity.FeedbackAnswer;
import com.feedback.feedback.system.entity.Question;
import com.feedback.feedback.system.entity.CourseAssignment;
import com.feedback.feedback.system.repository.QuestionRepository;
import com.feedback.feedback.system.service.FeedbackService;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FacultyController {

    @Autowired private FacultyRepository facultyRepo;
    @Autowired private CourseAssignmentRepository courseAssignmentRepo;
    @Autowired private FeedbackAnswerRepository feedbackAnswerRepo;
    @Autowired private QuestionRepository questionRepo;
    @Autowired private FeedbackService feedbackService;

    @GetMapping("/faculty/dashboard")
    public String facultyDashboard(Authentication auth, Model model) {
        String email = auth.getName();
        Faculty faculty = facultyRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));

        // Get faculty's course assignments
        List<CourseAssignment> courseAssignments = courseAssignmentRepo.findByFaculty(faculty);

        // Get feedback statistics for each course
        Map<Long, Map<String, Object>> courseStats = new HashMap<>();
        for (CourseAssignment ca : courseAssignments) {
            Map<String, Object> stats = new HashMap<>();

            // Count total students who gave feedback
            long feedbackCount = feedbackService.countFeedbacksForCourseAssignment(ca);
            stats.put("feedbackCount", feedbackCount);

            // Get question-wise statistics
            Map<String, Map<String, Long>> questionStats = new HashMap<>();
            Map<Question, Map<Integer, Long>> rawStats = feedbackService.getQuestionStatistics(ca);

            for (Map.Entry<Question, Map<Integer, Long>> entry : rawStats.entrySet()) {
                Question question = entry.getKey();
                Map<Integer, Long> ratings = entry.getValue();
                Map<String, Long> formattedRatings = new HashMap<>();

                // Initialize all ratings to 0
                for (int i = 1; i <= 5; i++) {
                    formattedRatings.put(String.valueOf(i), ratings.getOrDefault(i, 0L));
                }

                questionStats.put(String.valueOf(question.getQuestionId()), formattedRatings);
            }

            stats.put("questionStats", questionStats);

            // Get comments
            List<String> comments = feedbackService.getFeedbackComments(ca);
            stats.put("comments", comments);

            courseStats.put(ca.getAssignmentId(), stats);
        }

        model.addAttribute("faculty", faculty);
        model.addAttribute("courseAssignments", courseAssignments);
        model.addAttribute("courseStats", courseStats);
        model.addAttribute("questions", questionRepo.findAll());

        return "faculty-dashboard";
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyFeedbackApiController {
    @Autowired private FeedbackAnswerRepository feedbackAnswerRepo;
    @Autowired private FacultyRepository facultyRepo;
    @Autowired private QuestionRepository questionRepo;

    @GetMapping("/feedback-stats")
    @ResponseBody
    public Map<String, Object> getFeedbackStats(Authentication auth) {
        String email = auth.getName();
        Optional<Faculty> facultyOpt = facultyRepo.findByEmail(email);
        if (facultyOpt.isEmpty()) return Collections.emptyMap();
        Faculty faculty = facultyOpt.get();
        List<Question> questions = questionRepo.findAll();
        Map<String, Object> result = new LinkedHashMap<>();
        for (Question q : questions) {
            List<FeedbackAnswer> answers = feedbackAnswerRepo.findAll().stream()
                .filter(a -> a.getQuestion() != null && a.getQuestion().getQuestionId().equals(q.getQuestionId()))
                .filter(a -> a.getSession() != null && a.getSession().getFaculty() != null && a.getSession().getFaculty().getFacultyId().equals(faculty.getFacultyId()))
                .collect(Collectors.toList());
            double avg = answers.stream().mapToInt(FeedbackAnswer::getRating).average().orElse(0);
            List<String> comments = answers.stream().map(FeedbackAnswer::getAnswerText).collect(Collectors.toList());
            Map<String, Object> qStats = new HashMap<>();
            qStats.put("average", avg);
            qStats.put("comments", comments);
            result.put(q.getText(), qStats);
        }
        return result;
    }
}

