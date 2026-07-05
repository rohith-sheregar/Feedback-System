package com.feedback.feedback.system.repository;

import com.feedback.feedback.system.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;  // add this import

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    // Add this line:
    Optional<Faculty> findByEmail(String email);
}
