package com.onbelay.core.auth.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OBUser {

    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    public OBUser(
            String name,
            Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
