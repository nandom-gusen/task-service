package com.flowforge.security.jwt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.flowforge.utils.Utils.TOKEN_PREFIX;

@Data
public class JwtResponse implements Serializable {
    private String authorizationToken;
    private String type = TOKEN_PREFIX;
    private String userType;
    private String username;
    private String password;
    private List<String> roles;
    private List<String> permissions;

    public JwtResponse(String accessToken, String userType, String username, List<String> roles, List<String> permission) {
        this.authorizationToken = accessToken;
        this.userType = userType;
        this.username = username;
        this.roles = roles;
        this.permissions = permission;
    }
}

