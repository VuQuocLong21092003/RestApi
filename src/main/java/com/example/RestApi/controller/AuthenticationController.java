package com.example.RestApi.controller;

import com.example.RestApi.dto.request.AuthRequest;
import com.example.RestApi.dto.request.IntrospectRequest;
import com.example.RestApi.dto.response.ApiResponse;
import com.example.RestApi.dto.response.AuthResponse;
import com.example.RestApi.dto.response.IntrospectResponse;
import com.example.RestApi.service.UserService;
import com.example.RestApi.service.impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody @Validated AuthRequest authRequest) {
        AuthResponse response = authenticationService.authenticateUser(authRequest);
        return ApiResponse.<AuthResponse>builder()
                .status(200)
                .message("Login successful")
                .data(response)
                .build();
    }


    @PostMapping("/introspec")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody @Validated IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = authenticationService.introspectUser(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .status(200)
                .data(introspectResponse)
                .build();
    }
}
