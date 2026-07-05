package com.feedback.feedback.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.Principal;

import com.feedback.feedback.system.repository.StudentRepository;
import com.feedback.feedback.system.repository.CourseRepository;
import com.feedback.feedback.system.repository.FacultyRepository;
import com.feedback.feedback.system.repository.AdminRepository;
import com.feedback.feedback.system.entity.Admin;

@Controller
public class AdminController {

    @Autowired private StudentRepository studentRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private FacultyRepository facultyRepo;
    @Autowired private AdminRepository adminRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("studentCount", studentRepo.count());
        model.addAttribute("courseCount", courseRepo.count());
        model.addAttribute("facultyCount", facultyRepo.count());
        return "admin-dashboard";
    }

    @PostMapping("/admin/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/admin/dashboard";
        }

        Admin admin = adminRepo.findByUsername(principal.getName()).orElse(null);
        if (admin != null) {
            if (passwordEncoder.matches(oldPassword, admin.getPasswordHash())) {
                admin.setPasswordHash(passwordEncoder.encode(newPassword));
                adminRepo.save(admin);
                redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Incorrect old password.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Admin not found.");
        }
        
        return "redirect:/admin/dashboard";
    }

}
