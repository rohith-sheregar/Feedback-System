package com.feedback.feedback.system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";   // Thymeleaf template under src/main/resources/templates/login.html
    }

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        return switch(role) {
            case "ROLE_ADMIN"   -> "redirect:/admin/dashboard";
            case "ROLE_FACULTY" -> "redirect:/faculty/dashboard";
            case "ROLE_STUDENT" -> "redirect:/student/dashboard";
            default             -> "redirect:/login?error";
        };
    }
}
