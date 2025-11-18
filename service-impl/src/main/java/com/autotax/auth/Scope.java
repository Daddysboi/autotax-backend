package com.autotax.auth;

import lombok.Getter;

@Getter
public enum Scope {
    // Add scopes as needed, for example:
    USER_READ("user:read", "Read user information"),
    USER_WRITE("user:write", "Write user information");

    private final String code;
    private final String description;

    Scope(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
