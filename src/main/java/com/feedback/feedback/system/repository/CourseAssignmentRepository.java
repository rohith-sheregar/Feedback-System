package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.CourseAssignment;
import com.feedback.feedback.system.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Long> {
    List<CourseAssignment> findBySectionSectionId(Long sectionId);
    List<CourseAssignment> findByFacultyFacultyId(Long facultyId);
    List<CourseAssignment> findBySectionDepartmentDeptIdAndSectionSemester(Long deptId, Integer semester);
    boolean existsByCourseCourseIdAndSectionSectionIdAndFacultyFacultyId(Long courseId, Long sectionId, Long facultyId);
    List<CourseAssignment> findByFaculty(Faculty faculty);
}
