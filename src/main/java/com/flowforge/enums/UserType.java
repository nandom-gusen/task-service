package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
    USER("USER"),
    ADMIN("ADMIN");

    public final String label;
    private static final Map<String, UserType> map = new HashMap<>();

    static {
        for (UserType e : values()) {
            map.put(e.label, e);
        }
    }

    private UserType(String label) {
        this.label = label;
    }

    public static UserType valueOfName(String label) {
        return map.get(label);
    }
}

