package com.flowforge.utils;

import com.flowforge.exceptions.BadRequestException;

import java.util.Set;

public class Utils {

    public static final String TOKEN_PREFIX = "Bearer";
    public static final int BATCH_SIZE = 100;
    public static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "title", "description", "status", "priority", "createdAt", "completedAt", "dueDate"
    );

    public static void validateSortField(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException(
                    "Invalid sort field: " + sortBy + ". Allowed fields are: " +
                            String.join(", ", ALLOWED_SORT_FIELDS)
            );
        }
    }

    public static void validatePaginationParameters(int page, int size, String sortBy) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be negative");
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must be between 1 and " + MAX_PAGE_SIZE);
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            throw new BadRequestException("Sort field cannot be empty");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException(
                    "Invalid sort field: " + sortBy + ". Allowed fields are: " +
                            String.join(", ", ALLOWED_SORT_FIELDS)
            );
        }
    }
}
