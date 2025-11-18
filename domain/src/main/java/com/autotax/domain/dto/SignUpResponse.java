package com.autotax.domain.dto;

import com.autotax.domain.PortalUser;
import lombok.Data;

@Data
public class SignUpResponse {
    private String userId;
    private String authToken;
    private PortalUser portalUser;
}
