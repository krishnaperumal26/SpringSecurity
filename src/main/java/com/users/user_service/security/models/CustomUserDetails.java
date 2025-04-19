package com.users.user_service.security.models;

import com.users.user_service.models.Role;
import com.users.user_service.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    String username;
    String password;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
    boolean enabled;
    List<GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.authorities = new ArrayList<>();
        for(Role role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
