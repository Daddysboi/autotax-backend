package com.autotax.service.impl;

import com.autotax.service.JWTService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class JWTServiceImpl implements JWTService {

    @Override
    public String generateToken(String id, Map<String, Object> payload) {
        // TODO: Implement token generation logic
        return null;
    }

    @Override
    public String generateToken(String id, Map<String, Object> payload, LocalDateTime expirationTime) {
        // TODO: Implement token generation logic with expiration
        return null;
    }

    @Override
    public JWTClaimsSet decodeToken(String authHeader) throws JOSEException, ParseException {
        // TODO: Implement token decoding logic
        return null;
    }

    @Override
    public String getSerializedToken(String authHeader) {
        // TODO: Implement serialized token extraction logic
        return null;
    }
}
