package com.example.RestApi.service.impl;

import com.example.RestApi.dto.request.AuthRequest;
import com.example.RestApi.dto.request.IntrospectRequest;
import com.example.RestApi.dto.response.AuthResponse;
import com.example.RestApi.dto.response.IntrospectResponse;
import com.example.RestApi.exception.AppException;
import com.example.RestApi.exception.ErrorCode;
import com.example.RestApi.model.Users;
import com.example.RestApi.repository.UserRepository;
import com.example.RestApi.untils.DeletionStatus;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserRepository userRepository;


    @NonFinal
    private static final String SIGN = "5020f057d0d31c44d2397a3265c89b86b95a1903160610e290786cfe36e43e7b";

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Users user = userRepository.findByEmail(authRequest.getEmail());

        if (user.getDeletionStatus() == DeletionStatus.DELETED) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(Users user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("example.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour
                .claim("scope", user.getRole())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {

            jwsObject.sign(new MACSigner(SIGN));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

    }


    public IntrospectResponse introspectUser(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        JWSVerifier verifier = new MACVerifier(SIGN.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .active(verified && exp.after(new Date()))
                .build();
    }

}