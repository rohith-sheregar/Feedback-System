package com.feedback.feedback.system.service;

import com.feedback.feedback.system.entity.Admin;
import com.feedback.feedback.system.entity.Faculty;
import com.feedback.feedback.system.entity.Student;
import com.feedback.feedback.system.repository.AdminRepository;
import com.feedback.feedback.system.repository.FacultyRepository;
import com.feedback.feedback.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired private AdminRepository adminRepo;
    @Autowired private FacultyRepository facultyRepo;
    @Autowired private StudentRepository studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔍 Attempting to authenticate user: {}", username);

        try {
            // Try Admin
            Admin admin = adminRepo.findByUsername(username).orElse(null);
            if (admin != null) {
                log.info("✅ Found admin user: {}", username);
                return new User(admin.getUsername(), admin.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            }

            // Try Faculty
            Faculty faculty = facultyRepo.findByEmail(username).orElse(null);
            if (faculty != null) {
                log.info("✅ Found faculty user: {}", username);
                return new User(faculty.getEmail(), faculty.getPasswordHash(),
                        List.of(new SimpleGrantedAuthority("ROLE_FACULTY")));
            }

            // Try Student by USN first, then by email if USN fails
            Student student = studentRepo.findByUsn(username)
                    .orElseGet(() -> studentRepo.findByEmail(username).orElse(null));

            if (student != null) {
                log.info("✅ Found student: {} (USN: {})", student.getName(), student.getUsn());

                // Validate student data
                if (student.getPasswordHash() == null || student.getPasswordHash().trim().isEmpty()) {
                    log.error("❌ Student {} has no password hash", student.getUsn());
                    throw new UsernameNotFoundException("Invalid student account configuration");
                }

                return new User(
                    student.getUsn(),  // Always use USN as the principal name
                    student.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                );
            }

            log.error("❌ No user found with username/email: {}", username);
            throw new UsernameNotFoundException("No user found: " + username);

        } catch (Exception e) {
            log.error("❌ Error during authentication: {}", e.getMessage());
            throw new UsernameNotFoundException("Authentication error: " + e.getMessage());
        }
    }
}
