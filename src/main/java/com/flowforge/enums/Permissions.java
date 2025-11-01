package com.flowforge.enums;

import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    ///ROLE_USER
    CAN_VIEW_POST("CAN_VIEW_POST"),
    CAN_SEARCH_POST("CAN_SEARCH_POST"),
    CAN_ADD_POST("CAN_ADD_POST"),
    CAN_UPDATE_POST("CAN_UPDATE_POST"),
    CAN_DELETE_POST("CAN_DELETE_POST"),

    //ROLE_ADMIN
    CAN_VIEW_USER("CAN_VIEW_USER"),
    CAN_DELETE_USER("CAN_DELETE_USER"),
    CAN_UPDATE_USER("CAN_UPDATE_USER"),
    CAN_ADD_ROLE("CAN_ADD_ROLE"),
    CAN_UPDATE_ROLE("CAN_UPDATE_ROLE"),
    CAN_DELETE_ROLE("CAN_DELETE_ROLE");

    public final String label;
    private static final Map<String, Permissions> map = new HashMap<>();

    static {
        for (Permissions e : values()) {
            map.put(e.label, e);
        }
    }

    private Permissions(String label) {
        this.label = label;

    }

    public static Permissions valueOfName(String label) {
        return map.get(label);
    }
}

