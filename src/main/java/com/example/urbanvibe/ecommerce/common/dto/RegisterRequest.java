package com.example.urbanvibe.ecommerce.common.dto;

import com.example.urbanvibe.ecommerce.user.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}
