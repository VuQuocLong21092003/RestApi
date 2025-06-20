package com.example.RestApi.controller;

import com.example.RestApi.dto.request.UserRequest;
import com.example.RestApi.dto.response.ApiResponse;
import com.example.RestApi.dto.response.UserResponse;
import com.example.RestApi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ApiResponse<?> getUserUserName(@RequestParam("userName") String userName) {

        UserResponse userResponse = userService.findByUserName(userName);

        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get User by user name successfully")
                .data(userResponse)
                .build();
    }

    @GetMapping("/your-information")
    public ApiResponse<?> getYourInformation() {

        UserResponse userResponse = userService.findYourInformation();

        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get your information successfully")
                .data(userResponse)
                .build();
    }

    @PostMapping
    public ApiResponse<?> saveUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse savedUser = userService.save(userRequest);

        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(savedUser)
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<?> updateUser(@Valid @RequestBody UserRequest userRequest) {
        userService.update(userRequest);

        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Update User successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser( @PathVariable Long id ) {
        userService.delete(id);

        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Delete User successfully")
                .build();
    }
}
