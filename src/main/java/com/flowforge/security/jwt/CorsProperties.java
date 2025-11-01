package com.flowforge.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "allowed")
@Configuration
@Getter
@Setter
public class CorsProperties {
    private List<String> origins;
    private List<String> methods;
    private List<String> headers;
}
