package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    IN_REVIEW("IN_REVIEW"),
    DONE("DONE");

    public final String label;
    public static final Map<String, TaskStatus> map = new HashMap<>();

    static {
        for (TaskStatus e : values()) {
            map.put(e.label, e);
        }
    }

    private TaskStatus(String label) {

        this.label = label;
    }

    public static TaskStatus valueOfName(String label) {
        return map.get(label);
    }

}

