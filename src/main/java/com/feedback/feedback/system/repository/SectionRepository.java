package com.feedback.feedback.system.repository;


import com.feedback.feedback.system.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    // Find all sections for given department and semester
    List<Section> findByDepartmentDeptIdAndSemester(Long deptId, int semester);

    // Find a specific section by deptId, semester, and sectionName
    Optional<Section> findByDepartmentDeptIdAndSemesterAndSectionName(Long deptId, int semester, String sectionName);
}
