package com.autotax.domain.dto;

import lombok.Data;

@Data
public class AuthUserDto {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
}
