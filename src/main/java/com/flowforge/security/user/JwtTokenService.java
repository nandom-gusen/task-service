package com.flowforge.security.user;

import com.flowforge.security.jwt.JwtResponse;

public interface JwtTokenService {
    JwtResponse getAccessToken(String username, String password);
}
