package com.autotax.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddRolesAndPrivilegesDto {
    private List<String> roles;
    private List<String> privileges;
    // Add other fields as needed based on usage
}
