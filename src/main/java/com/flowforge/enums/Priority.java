package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum Priority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    public final String label;
    public static final Map<String, Priority> map = new HashMap<>();

    static {
        for (Priority e : values()) {
            map.put(e.label, e);
        }
    }

    private Priority(String label) {

        this.label = label;
    }

    public static Priority valueOfName(String label) {
        return map.get(label);
    }

}

