package com.flowforge.resource.metrics;

import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.HealthResponse;
import com.flowforge.dto.response.SystemMetrics;
import com.flowforge.service.metrics.HealthCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/health")
@Tag(name = "Application health resource", description = "Resource for simple none critical application metrics and health")
public class HealthCheckResource {

    private final HealthCheckService healthCheckService;

    public HealthCheckResource(HealthCheckService healthCheckService){
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    @Operation(
            summary = "App health",
            description = "Resource to check application health and metrics",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Application basic metrics are up and running",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<HealthResponse>> getHealth(){
        return healthCheckService.getHealth();
    }

    @GetMapping("/status")
    @Operation(
            summary = "Application status",
            description = "Resource to check Application status",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Application status",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<String>> getStatus() {
        return healthCheckService.getStatus();
    }

    @GetMapping("/database")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<String>> getDatabaseStatus() {
        return healthCheckService.getDatabaseStatus();
    }

    @GetMapping("/metrics")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<SystemMetrics>> getMetrics() {
        return healthCheckService.getMetrics();
    }

}
