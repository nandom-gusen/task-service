package com.flowforge;

import com.flowforge.enums.Priority;
import com.flowforge.enums.TaskStatus;
import com.flowforge.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskEntityTest {


    @Test
    public void shouldCreateTaskWithDefaultValues() {
        // Given
        Task task = new Task();

        // When
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        // Then
        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(Priority.LOW, task.getPriority());
        assertNull(task.getCreatedAt()); // Will be set on @PrePersist
    }

    @Test
    public void shouldSetCompletedAtWhenTaskIsDone() {
        // Given
        Task task = new Task();
        task.setTitle("Complete Task");
        task.setStatus(TaskStatus.TODO);

        // When
        task.setStatus(TaskStatus.DONE);
        task.onUpdate(); // Manually trigger @PreUpdate for testing

        // Then
        assertNotNull(task.getCompletedAt());
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void shouldNotSetCompletedAtForNonDoneStatus() {
        // Given
        Task task = new Task();
        task.setTitle("In Progress Task");
        task.setStatus(TaskStatus.IN_PROGRESS);

        // When
        task.onUpdate();

        // Then
        assertNull(task.getCompletedAt());
    }

    @Test
    void shouldSetCreatedAtOnCreate() {
        // Given
        Task task = new Task();
        task.setTitle("New Task");

        // When
        task.onCreate(); // Manually trigger @PrePersist

        // Then
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void shouldSetAllTaskProperties() {
        // Given
        Task task = new Task();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);

        // When
        task.setTitle("Full Task");
        task.setDescription("Complete description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setAssigneeId("USER_123");
        task.setWorkflowId("WORKFLOW_1");
        task.setDueDate(dueDate);
        task.setNotes("Important notes");

        // Then
        assertEquals("Full Task", task.getTitle());
        assertEquals("Complete description", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals("USER_123", task.getAssigneeId());
        assertEquals("WORKFLOW_1", task.getWorkflowId());
        assertEquals(dueDate, task.getDueDate());
        assertEquals("Important notes", task.getNotes());
    }
}
