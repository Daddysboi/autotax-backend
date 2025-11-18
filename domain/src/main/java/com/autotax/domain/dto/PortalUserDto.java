package com.autotax.domain.dto;

import lombok.Data;

@Data
public class PortalUserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
