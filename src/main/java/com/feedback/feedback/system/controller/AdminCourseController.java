package com.feedback.feedback.system.controller;

import com.feedback.feedback.system.entity.*;
import com.feedback.feedback.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCourseController {

    @Autowired private CourseRepository courseRepo;
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private FacultyRepository facultyRepo;
    @Autowired private SectionRepository sectionRepo;
    @Autowired private CourseAssignmentRepository assignRepo;

    // Show manage courses page
    @GetMapping("/manage-courses")
    public String manageCourses(@RequestParam(required = false) Long deptId,
                                @RequestParam(required = false) Integer semester,
                                Model model) {
        List<Department> depts = deptRepo.findAll();
        model.addAttribute("depts", depts);
        model.addAttribute("deptId", deptId);
        model.addAttribute("semester", semester);

        // Only show courses, sections, assignments if both filters are set
        List<Course> courses = List.of();
        List<Section> sections = List.of();
        List<CourseAssignment> assignments = List.of();
        if (deptId != null && semester != null) {
            courses = courseRepo.findByDepartment_DeptIdAndSemester(deptId, semester);
            sections = sectionRepo.findByDepartmentDeptIdAndSemester(deptId, semester);
            assignments = assignRepo.findBySectionDepartmentDeptIdAndSectionSemester(deptId, semester);
        }
        List<Faculty> faculty = facultyRepo.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("sections", sections);
        model.addAttribute("faculty", faculty);
        model.addAttribute("assignments", assignments);
        return "admin-manage-courses";
    }

    // Add a course
    @PostMapping("/courses")
    public String addCourse(@RequestParam String code,
                            @RequestParam String name,
                            @RequestParam Long deptId,
                            @RequestParam int semester) {
        Course c = new Course();
        c.setCourseCode(code);
        c.setCourseName(name);
        c.setDepartment(deptRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
        c.setSemester(semester);
        courseRepo.save(c);
        return "redirect:/admin/manage-courses";
    }

    // Update a course
    @PostMapping("/courses/update")
    public String updateCourse(@RequestParam Long courseId,
                               @RequestParam String courseCode,
                               @RequestParam String courseName,
                               @RequestParam Long deptId,
                               @RequestParam int semester) {
        Course c = courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        c.setCourseCode(courseCode);
        c.setCourseName(courseName);
        c.setDepartment(deptRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
        c.setSemester(semester);
        courseRepo.save(c);
        return "redirect:/admin/manage-courses";
    }

    // Delete a course
    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam Long courseId) {
        courseRepo.deleteById(courseId);
        return "redirect:/admin/manage-courses";
    }

    // Add section
    @PostMapping("/sections")
    public String addSection(@RequestParam String sectionName,
                             @RequestParam Long deptId,
                             @RequestParam int semester) {
        Section s = new Section();
        s.setSectionName(sectionName);
        s.setDepartment(deptRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
        s.setSemester(semester);
        sectionRepo.save(s);
        return "redirect:/admin/manage-courses";
    }

    // Add assignment (with duplicate check)
    @PostMapping("/assign")
    public String assignFaculty(@RequestParam Long courseId,
                                @RequestParam Long sectionId,
                                @RequestParam Long facultyId,
                                @RequestParam Long deptId,
                                @RequestParam int semester,
                                RedirectAttributes redirectAttributes) {
        boolean exists = assignRepo.existsByCourseCourseIdAndSectionSectionIdAndFacultyFacultyId(courseId, sectionId, facultyId);
        if (!exists) {
            CourseAssignment ca = new CourseAssignment();
            ca.setCourse(courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
            ca.setSection(sectionRepo.findById(sectionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found")));
            ca.setFaculty(facultyRepo.findById(facultyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found")));
            assignRepo.save(ca);
        }
        redirectAttributes.addAttribute("deptId", deptId);
        redirectAttributes.addAttribute("semester", semester);
        return "redirect:/admin/manage-courses";
    }

    // Remove assignment
    @PostMapping("/assign/delete")
    public String deleteAssignment(@RequestParam Long assignmentId,
                                   @RequestParam Long deptId,
                                   @RequestParam int semester,
                                   RedirectAttributes redirectAttributes) {
        assignRepo.deleteById(assignmentId);
        redirectAttributes.addAttribute("deptId", deptId);
        redirectAttributes.addAttribute("semester", semester);
        return "redirect:/admin/manage-courses";
    }

    // Edit assignment form
    @GetMapping("/assign/edit")
    public String editAssignmentForm(@RequestParam Long assignmentId, Model model) {
        CourseAssignment assignment = assignRepo.findById(assignmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));
        List<Department> depts = deptRepo.findAll();
        List<Faculty> faculty = facultyRepo.findAll();
        List<Course> courses = courseRepo.findByDepartment_DeptIdAndSemester(
            assignment.getSection().getDepartment().getDeptId(),
            assignment.getSection().getSemester()
        );
        List<Section> sections = sectionRepo.findByDepartmentDeptIdAndSemester(
            assignment.getSection().getDepartment().getDeptId(),
            assignment.getSection().getSemester()
        );
        model.addAttribute("assignment", assignment);
        model.addAttribute("depts", depts);
        model.addAttribute("faculty", faculty);
        model.addAttribute("courses", courses);
        model.addAttribute("sections", sections);
        model.addAttribute("deptId", assignment.getSection().getDepartment().getDeptId());
        model.addAttribute("semester", assignment.getSection().getSemester());
        return "admin-edit-assignment";
    }

    // Update assignment
    @PostMapping("/assign/update")
    public String updateAssignment(@RequestParam Long assignmentId,
                                   @RequestParam Long courseId,
                                   @RequestParam Long sectionId,
                                   @RequestParam Long facultyId,
                                   @RequestParam Long deptId,
                                   @RequestParam int semester,
                                   RedirectAttributes redirectAttributes) {
        CourseAssignment assignment = assignRepo.findById(assignmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));
        assignment.setCourse(courseRepo.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
        assignment.setSection(sectionRepo.findById(sectionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found")));
        assignment.setFaculty(facultyRepo.findById(facultyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found")));
        assignRepo.save(assignment);
        
        redirectAttributes.addAttribute("deptId", deptId);
        redirectAttributes.addAttribute("semester", semester);
        return "redirect:/admin/manage-courses";
    }
}
