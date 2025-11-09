package com.flowforge.service.task;

import com.flowforge.dto.request.TaskDTO;
import com.flowforge.dto.request.UpdateTaskDTO;
import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.TaskResponse;
import com.flowforge.exceptions.BadRequestException;
import com.flowforge.exceptions.RecordNotFoundException;
import com.flowforge.model.Task;
import com.flowforge.repository.task.TaskRepository;
import com.flowforge.utils.EntityMapperUtil;
import com.flowforge.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.flowforge.utils.AppMessages.*;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> createTask(TaskDTO taskDTO) {

        if(taskDTO == null){
            throw new BadRequestException("Task request body is invalid");
        }

        validateTitle(taskDTO.getTitle());

        Task task = EntityMapperUtil.mapTaskDTOtoTask(taskDTO);
        Task savedTask = taskRepository.save(task);
        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(savedTask);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<TaskResponse>>> getAllTasks(int page, int size, String sortBy, Sort.Direction sortDir) {

        //Utils.validateSortField(sortBy);
        Utils.validatePaginationParameters(page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        Page<Task> taskPage = taskRepository.findAll(pageable);

        Page<TaskResponse> responsePage = taskPage.map(EntityMapperUtil::mapTaskToTaskResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        responsePage)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> getTaskById(Long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            throw new RecordNotFoundException("Task with ID " + id + " not found");
            //throw new RecordNotFoundException("Oops! your task with ID " + id + " not found");
        }

        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(taskOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<TaskResponse>> updateTask(Long id, UpdateTaskDTO taskDTO) {

        if(taskDTO == null){
            throw new BadRequestException("Task request body is invalid");
            //throw new BadRequestException("Oops! your task request body is invalid");
        }

        if(taskDTO.getId() == null){
            throw new BadRequestException("Task ID is required in request body");
            //throw new RecordNotFoundException("Oops! your task with ID " + id + " not found in request body");
        }

        if(!taskDTO.getId().equals(id)){
            throw new BadRequestException("Task request IDs are mismatched");
            //throw new BadRequestException("Oops! your task request IDs are mismatched");
        }

        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            throw new RecordNotFoundException("Task with ID " + id + " not found");
            //throw new RecordNotFoundException("Oops! your task with ID " + id + " not found");
        }

        validateTitle(taskDTO.getTitle());

        Task task = EntityMapperUtil.mapUpdateTaskDTOtoTask(taskDTO);
        Task savedTask = taskRepository.save(task);

        TaskResponse taskResponse = EntityMapperUtil.mapTaskToTaskResponse(savedTask);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        taskResponse)
                );
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()){
            throw new RecordNotFoundException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateTitle(String title) {
        if (title == null) {
            throw new BadRequestException("Title is required");
            //throw new BadRequestException("Title is required");
            //Oops! Your request title parameter is either incomplete or invalid
        }

        String trimmedTitle = title.trim();
        if (trimmedTitle.isEmpty()) {
            //throw new BadRequestException("Oops! Your request title parameter is invalid");
            throw new BadRequestException("Title is required");
        }

        if (title.length() > 255) {
            throw new BadRequestException("Title must not exceed 255 characters");
            //throw new BadRequestException("Oops! Your request title characters exceeds 255");
        }
    }


}
