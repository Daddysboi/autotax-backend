package com.autotax.domain.dto;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    // Add other fields as needed based on usage
}
