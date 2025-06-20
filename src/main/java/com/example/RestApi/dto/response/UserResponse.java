package com.example.RestApi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserResponse implements Serializable {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;


}
