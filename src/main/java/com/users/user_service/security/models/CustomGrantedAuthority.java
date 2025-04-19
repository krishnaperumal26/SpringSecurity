package com.users.user_service.security.models;

import com.users.user_service.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {

    String authority;

    CustomGrantedAuthority(Role role) {
        this.authority = role.getValue();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
