package com.example.demo.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String token;
    private String message;
}
