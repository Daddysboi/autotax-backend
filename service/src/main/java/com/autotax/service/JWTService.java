package com.autotax.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;

public interface JWTService {

    String generateToken(String id, Map<String, Object> payload);

    String generateToken(String id, Map<String, Object> payload, LocalDateTime expirationTime);

    JWTClaimsSet decodeToken(String authHeader) throws JOSEException, ParseException;

    String getSerializedToken(String authHeader);

}
