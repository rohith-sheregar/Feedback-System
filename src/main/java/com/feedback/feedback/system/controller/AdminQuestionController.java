package com.feedback.feedback.system.controller;

import com.feedback.feedback.system.entity.Question;
import com.feedback.feedback.system.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminQuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/manage-questions")
    public String showQuestions(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "admin-manage-questions";
    }

    @PostMapping("/manage-questions/add")
    public String addQuestion(@RequestParam String questionText, RedirectAttributes redirectAttributes) {
        try {
            Question question = new Question();
            question.setText(questionText);
            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("success", "Question added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add question: " + e.getMessage());
        }
        return "redirect:/admin/manage-questions";
    }

    @PostMapping("/manage-questions/update")
    public String updateQuestion(
            @RequestParam Long questionId,
            @RequestParam String questionText,
            RedirectAttributes redirectAttributes) {
        try {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found"));
            question.setText(questionText);
            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("success", "Question updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update question: " + e.getMessage());
        }
        return "redirect:/admin/manage-questions";
    }

    @PostMapping("/manage-questions/delete")
    public String deleteQuestion(@RequestParam Long questionId, RedirectAttributes redirectAttributes) {
        try {
            if (!questionRepository.existsById(questionId)) {
                throw new IllegalArgumentException("Question not found");
            }
            questionRepository.deleteById(questionId);
            redirectAttributes.addFlashAttribute("success", "Question deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete question: " + e.getMessage());
        }
        return "redirect:/admin/manage-questions";
    }
}
