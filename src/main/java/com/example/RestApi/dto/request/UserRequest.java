package com.example.RestApi.dto.request;

import com.example.RestApi.untils.DeletionStatus;
import com.example.RestApi.untils.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest implements Serializable {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "password must not blank")
    @Size(min = 6, message = "password should have at least 6 characters")
    private String password;

    @NotBlank(message = "email must not blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "phone must not blank")
    private String phone;

    private String avatar;


}
