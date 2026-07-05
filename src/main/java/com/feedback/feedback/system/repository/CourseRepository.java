package com.feedback.feedback.system.repository;
import java.util.List;

import com.feedback.feedback.system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDepartment_DeptIdAndSemester(Long deptId, int semester);

}
