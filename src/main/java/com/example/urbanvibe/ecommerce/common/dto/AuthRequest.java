package com.example.urbanvibe.ecommerce.common.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
