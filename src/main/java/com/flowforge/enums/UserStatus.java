package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserStatus {
    INACTIVE("Inactive"),
    ACTIVE("Active"),
    ARCHIVED("Archived"),
    DELETED("Deleted");

    public final String label;
    private static final Map<String, UserStatus> map = new HashMap<>();

    static {
        for (UserStatus e : values()) {
            map.put(e.label, e);
        }
    }

    private UserStatus(String label) {
        this.label = label;
    }

    public static UserStatus valueOfName(String label) {
        return map.get(label);
    }
}

