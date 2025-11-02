package com.flowforge.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@TestConfiguration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SpringDataWebConfig {

}
