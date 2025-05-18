package com.example.demo.dto;

import lombok.Data;

@Data
public class TpoRegisterRequest {
    private String name;
    private String idNumber;
    private String designation;
    private String campus;
    private String phone;
    private String collegeEmail;
    private String password;
}

