package com.example.RestApi.service.impl;

import com.example.RestApi.dto.request.UserRequest;
import com.example.RestApi.dto.response.UserResponse;
import com.example.RestApi.exception.AppException;
import com.example.RestApi.exception.ErrorCode;
import com.example.RestApi.model.Users;
import com.example.RestApi.repository.UserRepository;
import com.example.RestApi.service.UserService;
import com.example.RestApi.untils.DeletionStatus;
import com.example.RestApi.untils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse findByUserName(String userName) {


        if(!userRepository.existsByUsername(userName)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findByUsername(userName);

        if (user == null || user.getDeletionStatus() == DeletionStatus.DELETED) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public UserResponse findYourInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if(!userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findByEmail(email);


        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        Users user = new Users();

        try {
            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setPassword(userRequest.getPassword());
            user.setEmail(userRequest.getEmail());
            user.setPhone(userRequest.getPhone());
            user.setAvatar(userRequest.getAvatar());
            user.setRole(Role.USER);
            user.setDeletionStatus(DeletionStatus.ACTIVE);
            user = userRepository.save(user);
        } catch (Exception e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }


        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public void update(UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if(!userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Users user = userRepository.findByEmail(email);

        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAvatar(userRequest.getAvatar());
        user.setRole(Role.USER);
        user.setDeletionStatus(DeletionStatus.ACTIVE);
        user = userRepository.save(user);

    }

    @Override
    public void delete(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setDeletionStatus(DeletionStatus.DELETED);
        user = userRepository.save(user);

    }


}