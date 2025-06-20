package com.example.RestApi.service;

import com.example.RestApi.dto.request.UserRequest;
import com.example.RestApi.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse findByUserName(String userName);

    UserResponse save(UserRequest userRequest);

    void update( UserRequest userRequest);

    void delete(Long id);

    UserResponse findYourInformation();



}
