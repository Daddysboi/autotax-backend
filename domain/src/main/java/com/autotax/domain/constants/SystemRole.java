package com.autotax.domain.constants;

public enum SystemRole {
    // Add system roles as needed, for example:
    ADMIN("admin"),
    USER("user");

    private final String roleName;

    SystemRole(String roleName) {
        this.roleName = roleName;
    }

    public String roleName() {
        return roleName;
    }
}
