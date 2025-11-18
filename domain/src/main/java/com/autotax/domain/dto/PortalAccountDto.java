package com.autotax.domain.dto;

import com.autotax.domain.constants.PortalAccountTypeConstant;
import lombok.Data;

@Data
public class PortalAccountDto {
    private String name;
    private PortalAccountTypeConstant type;
    private String email;
    private String phoneNumber;
    private String altPhoneNumber;
    private String code;
}
