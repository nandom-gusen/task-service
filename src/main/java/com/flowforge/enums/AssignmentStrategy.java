package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum AssignmentStrategy {
    ROUND_ROBIN("ROUND_ROBIN"),
    LOAD_BASED("LOAD_BASED"),
    MANUAL("MANUAL");

    public final String label;
    public static final Map<String, AssignmentStrategy> map = new HashMap<>();

    static {
        for (AssignmentStrategy e : values()) {
            map.put(e.label, e);
        }
    }

    private AssignmentStrategy(String label) {

        this.label = label;
    }

    public static AssignmentStrategy valueOfName(String label) {
        return map.get(label);
    }

}

