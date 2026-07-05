package com.feedback.feedback.system.config;

import com.feedback.feedback.system.entity.Admin;
import com.feedback.feedback.system.entity.Faculty;
import com.feedback.feedback.system.entity.Student;
import com.feedback.feedback.system.repository.AdminRepository;
import com.feedback.feedback.system.repository.FacultyRepository;
import com.feedback.feedback.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigrationConfig implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Migrate Admin passwords
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            if (admin.getPasswordHash() != null && !admin.getPasswordHash().startsWith("$2a$")) {
                admin.setPasswordHash(passwordEncoder.encode(admin.getPasswordHash()));
                adminRepository.save(admin);
            }
        }

        // Migrate Faculty passwords
        List<Faculty> faculties = facultyRepository.findAll();
        for (Faculty faculty : faculties) {
            if (faculty.getPasswordHash() != null && !faculty.getPasswordHash().startsWith("$2a$")) {
                faculty.setPasswordHash(passwordEncoder.encode(faculty.getPasswordHash()));
                facultyRepository.save(faculty);
            }
        }

        // Migrate Student passwords
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            if (student.getPasswordHash() != null && !student.getPasswordHash().startsWith("$2a$")) {
                student.setPasswordHash(passwordEncoder.encode(student.getPasswordHash()));
                studentRepository.save(student);
            }
        }
    }
}
