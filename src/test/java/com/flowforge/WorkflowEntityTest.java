package com.flowforge;

import com.flowforge.enums.TaskStatus;
import com.flowforge.model.Workflow;
import com.flowforge.model.WorkflowState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WorkflowEntityTest {

    @Test
    void shouldCreateWorkflowWithDefaultValues() {
        // Given
        Workflow workflow = new Workflow();

        // When
        workflow.setName("Test Workflow");
        workflow.setDescription("Test Description");

        // Then
        assertNotNull(workflow);
        assertEquals("Test Workflow", workflow.getName());
        assertEquals("Test Description", workflow.getDescription());
        assertTrue(workflow.getIsActive());
        assertNotNull(workflow.getStates());
        assertTrue(workflow.getStates().isEmpty());
    }

    @Test
    void shouldSetCreatedAtOnCreate() {
        // Given
        Workflow workflow = new Workflow();
        workflow.setName("New Workflow");

        // When
        workflow.onCreate(); // Manually trigger @PrePersist

        // Then
        assertNotNull(workflow.getCreatedAt());
    }

    @Test
    void shouldAddStatesToWorkflow() {
        // Given
        Workflow workflow = new Workflow();
        workflow.setName("Workflow with States");

        WorkflowState state1 = new WorkflowState();
        state1.setStatus(TaskStatus.TODO);
        state1.setRequiresAssignee(false);

        WorkflowState state2 = new WorkflowState();
        state2.setStatus(TaskStatus.IN_PROGRESS);
        state2.setRequiresAssignee(true);

        // When
        workflow.addState(state1);
        workflow.addState(state2);

        // Then
        assertEquals(2, workflow.getStates().size());
        assertEquals(workflow, state1.getWorkflow());
        assertEquals(workflow, state2.getWorkflow());
    }

    @Test
    void shouldSetAllWorkflowProperties() {
        // Given
        Workflow workflow = new Workflow();

        // When
        workflow.setName("Complete Workflow");
        workflow.setDescription("Full description");
        workflow.setIsActive(false);

        // Then
        assertEquals("Complete Workflow", workflow.getName());
        assertEquals("Full description", workflow.getDescription());
        assertFalse(workflow.getIsActive());
    }
}
