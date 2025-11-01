package com.flowforge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMetrics {
    private String memoryUsed;
    private String memoryTotal;
    private int processors;
    private String javaVersion;
    private String serviceName;
}
