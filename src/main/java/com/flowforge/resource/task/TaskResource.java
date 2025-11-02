package com.flowforge.resource.task;

import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.model.Task;
import com.flowforge.service.task.TaskService;
import com.flowforge.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import com.flowforge.dto.request.TaskDTO;


import static com.flowforge.utils.Utils.MAX_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task resource", description = "Resource for Tasks")
public class TaskResource {

    private final TaskService taskService;



    public TaskResource(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(
            summary = "Task Creation",
            description = "Resource to create task",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resource to create task",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<TaskResponse>> createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @GetMapping
    @Operation(
            summary = "Retrieve all tasks with pagination",
            description = "Fetches a paginated list of tasks with support for sorting and filtering. " +
                    "Returns task metadata including total count, page information, and navigation links."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<Page<TaskResponse>>> getAllTasks(
            @Parameter(description = "Page number (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Number of records per page", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) int size,

            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir
    ) {
        return taskService.getAllTasks(page, size, sortBy, sortDir);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a specific task by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task found and returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<TaskResponse>> getTaskById(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        return taskService.getTaskById(id);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing task",
            description = "Updates all fields of an existing task. Full task object must be provided."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<TaskResponse>> updateTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDTO taskDTO
    ) {
        return taskService.updateTask(id, taskDTO);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a task",
            description = "Permanently deletes a task by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deleted successfully"
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id
    ) {
        return taskService.deleteTask(id);

    }
}
