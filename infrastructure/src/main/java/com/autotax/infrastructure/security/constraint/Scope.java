package com.autotax.infrastructure.security.constraint;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Sep, 2020
 **/
public
enum Scope {
    CREATE_USER("byteone.user.write", "Create a new user");

    private String code;
    private String description;

    Scope(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
