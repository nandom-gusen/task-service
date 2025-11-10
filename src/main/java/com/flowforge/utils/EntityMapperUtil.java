package com.flowforge.utils;

import com.flowforge.dto.request.AssignmentDTO;
import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.AssignmentResponse;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.enums.AssignmentStrategy;
import com.flowforge.enums.Priority;
import com.flowforge.enums.TaskStatus;
import com.flowforge.model.Assignment;
import com.flowforge.model.Task;

public class EntityMapperUtil {

    public static Task mapTaskDTOtoTask(TaskDTO taskDTO){
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setNotes(taskDTO.getNotes());
        task.setAssigneeId(taskDTO.getAssigneeId());
        task.setWorkflowId(taskDTO.getWorkflowId());
        task.setCreatedAt(taskDTO.getCreatedAt());
        task.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        }

        if (taskDTO.getPriority() != null) {
            task.setPriority(Priority.valueOf(taskDTO.getPriority()));
        }

        return task;
    }

    public static Task mapUpdateTaskDTOtoTask(UpdateTaskDTO taskDTO){
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setNotes(taskDTO.getNotes());
        task.setAssigneeId(taskDTO.getAssigneeId());
        task.setWorkflowId(taskDTO.getWorkflowId());
        task.setCreatedAt(taskDTO.getCreatedAt());
        task.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        }

        if (taskDTO.getPriority() != null) {
            task.setPriority(Priority.valueOf(taskDTO.getPriority()));
        }

        return task;
    }

    public static TaskResponse mapTaskToTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setNotes(task.getNotes());
        taskResponse.setAssigneeId(task.getAssigneeId());
        taskResponse.setWorkflowId(task.getWorkflowId());
        taskResponse.setCreatedAt(task.getCreatedAt());
        taskResponse.setDueDate(task.getDueDate());

        if (task.getStatus() != null) {
            taskResponse.setStatus(task.getStatus().label);
        }

        if (task.getPriority() != null) {
            taskResponse.setPriority(task.getPriority().label);
        }

        return taskResponse;
    }

    public static Assignment mapAssignmentDTOtoAssignment(AssignmentDTO assignmentDTO){
        Assignment assignment = new Assignment();
        assignment.setTaskId(assignmentDTO.getTaskId());
        assignment.setAssignedBy(assignmentDTO.getAssignedBy());
        assignment.setAssigneeId(assignmentDTO.getAssigneeId());
        assignment.setReason(assignmentDTO.getReason());


        if (assignmentDTO.getStrategy() != null) {
            assignment.setStrategy(AssignmentStrategy.valueOf(assignmentDTO.getStrategy()));
        }

        return assignment;
    }

    public static AssignmentResponse mapAssignmentToAssignmentResponse(Assignment assignment){
        AssignmentResponse assignmentResponse = new AssignmentResponse();
        assignmentResponse.setId(assignment.getId());
        assignmentResponse.setAssigneeId(assignment.getAssigneeId());
        assignmentResponse.setAssignedBy(assignment.getAssignedBy());
        assignmentResponse.setTaskId(assignment.getTaskId());
        assignmentResponse.setAssignedAt(assignment.getAssignedAt());
        assignmentResponse.setReason(assignment.getReason());


        if (assignment.getStrategy() != null) {
            assignmentResponse.setStrategy(assignment.getStrategy().label);
        }

        return assignmentResponse;
    }


}
