package com.flowforge.security.user;

import com.flowforge.enums.UserType;
import com.flowforge.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {
    private String name;
    private String userType;
    private String userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String name, String userType, String userId, String username, String password,
                           Collection<? extends GrantedAuthority> roles,
                           Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.userId = userId;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }


    public static UserDetailsImpl build(User users) {
        return new UserDetailsImpl(
                (users.getFirstName() + " " + users.getLastName()),
                users.getUserType().label,
                users.getUserId(),
                users.getEmail(),
                users.getPassword(),
                processUserRoles(),
                processUserAuthorities());
    }

    private static List<GrantedAuthority> processUserRoles() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + UserType.USER.name()));
    }

    private static List<GrantedAuthority> processUserAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + UserType.USER.name()));
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

