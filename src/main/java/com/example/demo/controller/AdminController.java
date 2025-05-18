package com.example.demo.controller;

import com.example.demo.dto.AdminRegisterRequest;

import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResponse;

import com.example.demo.service.AdminService;

import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // for frontend connection
public class AdminController {

    private final AdminService adminService;

   
	
    @PostMapping("/adminregister")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegisterRequest request) {
        String result = adminService.registerAdmin(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/adminlogin")
public ResponseEntity<AdminLoginResponse> loginAdmin(@RequestBody AdminLoginRequest request) {
    return ResponseEntity.ok(adminService.loginAdmin(request));
}


@GetMapping("/test")
    public ResponseEntity<String> testProtected() {
        return ResponseEntity.ok("✅ Protected endpoint is working!");
    }



}
