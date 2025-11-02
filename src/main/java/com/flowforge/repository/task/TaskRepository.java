package com.flowforge.repository.task;

import com.flowforge.enums.Priority;
import com.flowforge.enums.TaskStatus;
import com.flowforge.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository  extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByAssigneeId(String assigneeId);

    List<Task> findByWorkflowId(String workflowId);
}
