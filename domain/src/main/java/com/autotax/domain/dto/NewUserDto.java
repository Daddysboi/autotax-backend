package com.autotax.domain.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    // Add other fields as needed based on usage
}
