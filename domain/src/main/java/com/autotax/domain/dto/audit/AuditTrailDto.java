package com.autotax.domain.dto.audit;

import lombok.Data;

@Data
public class AuditTrailDto {
    public enum RecordTypeEnum {
        PRIVILEGE_ASSIGNMENT,
        PRIVILEGE_REMOVAL
        // Add other record types as needed
    }
    // Add other fields as needed
}
