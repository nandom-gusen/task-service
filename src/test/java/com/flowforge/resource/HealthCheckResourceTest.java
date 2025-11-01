package com.flowforge.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HealthCheckResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnHealthStatusWithMetrics() throws Exception {
        mockMvc.perform(get("/api/v1/app/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.message").value("Application is running"))
                .andExpect(jsonPath("$.data.timestamp").exists())
                .andExpect(jsonPath("$.data.database").exists())
                .andExpect(jsonPath("$.data.database").value(containsString("Connected")))
                .andExpect(jsonPath("$.data.systemMetrics").exists())
                .andExpect(jsonPath("$.data.systemMetrics.memoryUsed").exists())
                .andExpect(jsonPath("$.data.systemMetrics.processors").isNumber());
    }

    @Test
    void shouldReturnSimpleStatus() throws Exception {
        mockMvc.perform(get("/api/v1/app/health/status"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnDatabaseStatus() throws Exception {
        mockMvc.perform(get("/api/v1/app/health/database"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("H2")))
                .andExpect(content().string(containsString("Connected")));
    }

    @Test
    void shouldReturnSystemMetrics() throws Exception {
        mockMvc.perform(get("/api/v1/app/health/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memoryUsed").exists())
                .andExpect(jsonPath("$.data.memoryTotal").exists())
                .andExpect(jsonPath("$.data.processors").isNumber())
                .andExpect(jsonPath("$.data.javaVersion").exists())
                .andExpect(jsonPath("$.data.serviceName").value("Task Management Service"));
    }
}
