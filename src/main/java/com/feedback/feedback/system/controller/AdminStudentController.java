package com.feedback.feedback.system.controller;

import com.feedback.feedback.system.entity.Department;
import com.feedback.feedback.system.entity.Section;
import com.feedback.feedback.system.entity.Student;
import com.feedback.feedback.system.repository.DepartmentRepository;
import com.feedback.feedback.system.repository.SectionRepository;
import com.feedback.feedback.system.repository.StudentRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminStudentController {

    private static final Logger logger = LoggerFactory.getLogger(AdminStudentController.class);

    @Autowired private StudentRepository studentRepo;
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private SectionRepository sectionRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    // Show the manage-students page
    @GetMapping("/manage-students")
    public String manageStudents(
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Long sectionId,
            Model model) {

        logger.debug("Accessing manage students page with deptId={}, semester={}, sectionId={}", deptId, semester, sectionId);

        List<Department> depts = deptRepo.findAll();
        List<Section> sections = sectionRepo.findAll();
        List<Student> students;

        if (deptId == null && semester == null && sectionId == null) {
            students = studentRepo.findAll();
        } else {
            students = studentRepo.findByFilters(deptId, semester, sectionId);
        }

        model.addAttribute("students", students);
        model.addAttribute("depts", depts);
        model.addAttribute("sections", sections);
        model.addAttribute("deptId", deptId);
        model.addAttribute("semester", semester);
        model.addAttribute("sectionId", sectionId);

        return "admin-manage-students";
    }

    // Show CSV upload page
    @GetMapping("/upload-students")
    public String showUploadForm(Model model) {
        model.addAttribute("depts", deptRepo.findAll());
        model.addAttribute("sections", sectionRepo.findAll());
        return "upload-students";
    }

    // Delete a student
    @PostMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return "redirect:/admin/manage-students";
    }

    // Add a new student manually
    @PostMapping("/students")
    public String addStudent(@RequestParam String usn,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam Long deptId,
                             @RequestParam int semester,
                             @RequestParam Long sectionId) {

        Student s = new Student();
        s.setUsn(usn);
        s.setName(name);
        s.setEmail(email);
        s.setPasswordHash(passwordEncoder.encode(usn)); // default password encoded
        s.setDepartment(deptRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
        s.setSemester(semester);
        s.setSection(sectionRepo.findById(sectionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found")));

        studentRepo.save(s);
        return "redirect:/admin/manage-students";
    }

    // delete all functionality
    @PostMapping("/students/delete-all")
    public String deleteAllStudents(RedirectAttributes redirectAttributes) {
        studentRepo.deleteAll();
        redirectAttributes.addFlashAttribute("message", "🗑️ All students deleted successfully.");
        return "redirect:/admin/manage-students";
    }

    // Upload students from CSV
    @PostMapping("/upload-students")
    public String uploadStudentsCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            redirectAttributes.addFlashAttribute("message", "❌ Please upload a valid CSV file.");
            return "redirect:/admin/manage-students";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String usn = record.get("usn").trim();
                String name = record.get("name").trim();
                String email = record.get("email").trim();
                String password = record.get("password").trim();
                Long deptId = Long.parseLong(record.get("dept_id"));
                int semester = Integer.parseInt(record.get("semester"));
                String sectionName = record.get("section").trim(); // section name from CSV

                if (studentRepo.existsByUsn(usn) || studentRepo.existsByEmail(email)) {
                    continue; // Skip duplicates
                }

                Optional<Department> departmentOpt = deptRepo.findById(deptId);
                Optional<Section> sectionOpt = sectionRepo.findByDepartmentDeptIdAndSemesterAndSectionName(deptId, semester, sectionName);

                if (departmentOpt.isEmpty() || sectionOpt.isEmpty()) {
                    logger.warn("Skipping student {} due to missing department or section.", usn);
                    continue;
                }

                Student s = new Student();
                s.setUsn(usn);
                s.setName(name);
                s.setEmail(email);
                s.setPasswordHash(passwordEncoder.encode(password));
                s.setDepartment(departmentOpt.get());
                s.setSemester(semester);
                s.setSection(sectionOpt.get());

                studentRepo.save(s);
            }

            redirectAttributes.addFlashAttribute("message", "✅ Students uploaded successfully.");
            return "redirect:/admin/manage-students";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "❌ Failed to upload students: " + e.getMessage());
            return "redirect:/admin/manage-students";
        }
    }
}
