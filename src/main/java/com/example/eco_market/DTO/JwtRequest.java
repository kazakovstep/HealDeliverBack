package com.example.eco_market.DTO;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
