package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Check for USN or email existence
    Optional<Student> findByUsn(String usn);
    Optional<Student> findByEmail(String email); // Added
    boolean existsByUsn(String usn);
    boolean existsByEmail(String email);

    // Filter students by optional department, semester, and section
    @Query("SELECT s FROM Student s " +
            "WHERE (:deptId IS NULL OR s.department.deptId = :deptId) " +
            "AND (:semester IS NULL OR s.semester = :semester) " +
            "AND (:sectionId IS NULL OR s.section.sectionId = :sectionId)")
    List<Student> findByFilters(@Param("deptId") Long deptId,
                                @Param("semester") Integer semester,
                                @Param("sectionId") Long sectionId);

    // Optional: Delete all students at once
    void deleteAll();
}