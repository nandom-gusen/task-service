package com.flowforge.service.metrics;

import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.HealthResponse;
import com.flowforge.dto.response.SystemMetrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;

import static com.flowforge.utils.AppMessages.GENERIC_SUCCESS_MESSAGE;

@Service
public class HealthCheckServiceImpl implements HealthCheckService{

    private final DataSource dataSource;

    public HealthCheckServiceImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity<ApiResponseDto<HealthResponse>> getHealth() {

        String dbStatus = checkDatabaseConnection();
        SystemMetrics metrics = collectSystemMetrics();

        HealthResponse response = new HealthResponse(
                "UP",
                "Application is running",
                LocalDateTime.now(),
                dbStatus,
                metrics
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                                HttpStatus.OK.value(),
                                HttpStatus.OK,
                                GENERIC_SUCCESS_MESSAGE,
                                response)
                );

    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> getStatus() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        "UP")
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> getDatabaseStatus() {

        String dbStatus = checkDatabaseConnection();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        dbStatus)
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<SystemMetrics>> getMetrics() {

        SystemMetrics metrics = collectSystemMetrics();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        GENERIC_SUCCESS_MESSAGE,
                        metrics)
                );
    }


    private String checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            String dbType = connection.getMetaData().getDatabaseProductName();
            return dbType + " - Connected";
        } catch (Exception e) {
            return "Database - Disconnected";
        }
    }

    private SystemMetrics collectSystemMetrics() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        int availableProcessors = runtime.availableProcessors();

        return new SystemMetrics(
                formatBytes(usedMemory),
                formatBytes(totalMemory),
                availableProcessors,
                System.getProperty("java.version"),
                "Task Management Service"
        );
    }

    private String formatBytes(long bytes) {
        long mb = bytes / (1024 * 1024);
        return mb + " MB";
    }

}
