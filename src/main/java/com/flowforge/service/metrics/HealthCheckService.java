package com.flowforge.service.metrics;

import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.HealthResponse;
import com.flowforge.dto.response.SystemMetrics;
import org.springframework.http.ResponseEntity;

public interface HealthCheckService {
    ResponseEntity<ApiResponseDto<HealthResponse>> getHealth();
    ResponseEntity<ApiResponseDto<String>> getStatus();
    ResponseEntity<ApiResponseDto<String>> getDatabaseStatus();
    ResponseEntity<ApiResponseDto<SystemMetrics>> getMetrics();
}
