package com.flowforge.service;

import com.flowforge.dto.response.ApiResponseDto;
import com.flowforge.dto.response.HealthResponse;
import com.flowforge.dto.response.SystemMetrics;
import com.flowforge.service.metrics.HealthCheckServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class HealthCheckServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData metaData;

    @InjectMocks
    private HealthCheckServiceImpl healthCheckService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getHealth_shouldReturnHealthResponseWithMetrics() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("H2");

        // Act
        ResponseEntity<ApiResponseDto<HealthResponse>> response = healthCheckService.getHealth();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        HealthResponse healthResponse = response.getBody().getData();
        assertNotNull(healthResponse);
        assertEquals("UP", healthResponse.getStatus());
        assertEquals("Application is running", healthResponse.getMessage());
        assertNotNull(healthResponse.getTimestamp());
        assertTrue(healthResponse.getDatabase().contains("H2"));
        assertTrue(healthResponse.getDatabase().contains("Connected"));

        SystemMetrics metrics = healthResponse.getSystemMetrics();
        assertNotNull(metrics);
        assertNotNull(metrics.getMemoryUsed());
        assertNotNull(metrics.getMemoryTotal());
        assertTrue(metrics.getProcessors() > 0);
        assertNotNull(metrics.getJavaVersion());
        assertEquals("Task Management Service", metrics.getServiceName());

        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).close();
    }

    @Test
    void getStatus_shouldReturnUpStatus() {
        // Act
        ResponseEntity<ApiResponseDto<String>> response = healthCheckService.getStatus();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("UP", response.getBody().getData());
    }

    @Test
    void getDatabaseStatus_shouldReturnConnectedStatus() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("H2");

        // Act
        ResponseEntity<ApiResponseDto<String>> response = healthCheckService.getDatabaseStatus();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        String dbStatus = response.getBody().getData();
        assertNotNull(dbStatus);
        assertTrue(dbStatus.contains("H2"));
        assertTrue(dbStatus.contains("Connected"));

        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).close();
    }

    @Test
    void getDatabaseStatus_shouldReturnDisconnectedWhenException() throws Exception {
        // Arrange
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Connection failed"));

        // Act
        ResponseEntity<ApiResponseDto<String>> response = healthCheckService.getDatabaseStatus();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        String dbStatus = response.getBody().getData();
        assertNotNull(dbStatus);
        assertEquals("Database - Disconnected", dbStatus);

        verify(dataSource, times(1)).getConnection();
    }

    @Test
    void getMetrics_shouldReturnSystemMetrics() {
        // Act
        ResponseEntity<ApiResponseDto<SystemMetrics>> response = healthCheckService.getMetrics();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());

        SystemMetrics metrics = response.getBody().getData();
        assertNotNull(metrics);
        assertNotNull(metrics.getMemoryUsed());
        assertNotNull(metrics.getMemoryTotal());
        assertTrue(metrics.getProcessors() > 0);
        assertNotNull(metrics.getJavaVersion());
        assertEquals("Task Management Service", metrics.getServiceName());

        // Verify memory format
        assertTrue(metrics.getMemoryUsed().endsWith("MB"));
        assertTrue(metrics.getMemoryTotal().endsWith("MB"));
    }
}
