package com.flowforge.utils;

import java.util.Set;

public class Utils {

    public static final String TOKEN_PREFIX = "Bearer";
    public static final int BATCH_SIZE = 100;
    public static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "title", "description", "status", "priority", "createdAt", "updatedAt", "dueDate"
    );

    public static void validateSortField(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy +
                            ". Allowed fields: " + ALLOWED_SORT_FIELDS
            );
        }
    }
}
