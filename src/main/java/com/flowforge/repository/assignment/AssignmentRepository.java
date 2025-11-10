package com.flowforge.repository.assignment;

import com.flowforge.model.Assignment;
import com.flowforge.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByTaskId(Long taskId);
}
