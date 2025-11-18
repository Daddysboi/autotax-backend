package com.autotax.service.impl;

import com.autotax.domain.service.AppConfigurationProperties;
import com.autotax.service.JWTService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTServiceImpl implements JWTService {
    private final AppConfigurationProperties appConfigurationProperties;
    private final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);



    @Override
    public String generateToken(String id, Map<String, Object> payload) {
        return generateToken(id, payload, LocalDateTime.now().plus(10, ChronoUnit.DECADES));
    }

    @Override
    public String generateToken(String id, Map<String, Object> payload, LocalDateTime expirationTime) {
        String host = "";

        JWTClaimsSet.Builder claimBuilder = new JWTClaimsSet.Builder();
        claimBuilder.expirationTime(Timestamp.valueOf(expirationTime));
        claimBuilder.issueTime(Timestamp.valueOf(LocalDateTime.now()));
        claimBuilder.issuer(host);
        claimBuilder.subject(String.valueOf(id));

        for (String key : payload.keySet()) {
            claimBuilder.claim(key, payload.get(key));
        }

        JWTClaimsSet claimsSet = claimBuilder.build();
        JWSSigner signer = null;
        String token = null;
        try {
            signer = new MACSigner(appConfigurationProperties.getCustomAuthTokenSecret());
            SignedJWT jwt = new SignedJWT(JWT_HEADER, claimsSet);
            jwt.sign(signer);
            token = jwt.serialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to generate token");
        }
        return token;
    }

    @Override
    public JWTClaimsSet decodeToken(String authHeader) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(getSerializedToken(authHeader));
        if (signedJWT.verify(new MACVerifier(appConfigurationProperties.getCustomAuthTokenSecret()))) {

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new JOSEException("Token Expired");
            }
            return signedJWT.getJWTClaimsSet();
        } else {
            throw new JOSEException("Signature verification failed");
        }
    }

    @Override
    public String getSerializedToken(String authHeader) {
        return authHeader.replaceAll("Bearer ", "").replaceAll("bearer ", "");
    }

}
