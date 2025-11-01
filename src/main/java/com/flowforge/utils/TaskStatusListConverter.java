package com.flowforge.utils;

import com.flowforge.enums.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class TaskStatusListConverter implements AttributeConverter<List<TaskStatus>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<TaskStatus> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<TaskStatus> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(TaskStatus::valueOf)
                .collect(Collectors.toList());
    }
}

