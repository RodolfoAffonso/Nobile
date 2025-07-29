package com.rodolfoafonso.nobile.domain.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum UserRole {
    ADMIN(List.of("ADMIN", "USER")),
    SELLER(List.of("SELLER", "USER")),
    BUYER(List.of("BUYER", "USER")),
    USER(List.of("USER"));

    private final List<String> authorities;

    UserRole(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<String> getAuthorities() {
        return authorities.stream()
                .map(role -> "ROLE_" + role)
                .toList();
    }
}
