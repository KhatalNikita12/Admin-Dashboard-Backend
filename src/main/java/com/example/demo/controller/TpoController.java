package com.example.demo.controller;

import com.example.demo.dto.TpoLoginRequest;
import com.example.demo.dto.TpoLoginResponse;
import com.example.demo.dto.TpoRegisterRequest;
import com.example.demo.service.TpoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/tpo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // for frontend connection
public class TpoController {

    private final TpoService tpoService;

    @GetMapping("/home")
	public String home() {
		return "Welcome page this is admin side";
	}
	
    @PostMapping("/register")
    public ResponseEntity<String> registerTpo(@RequestBody TpoRegisterRequest request) {
        String result = tpoService.registerTpo(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
public ResponseEntity<TpoLoginResponse> loginTpo(@RequestBody TpoLoginRequest request) {
    return ResponseEntity.ok(tpoService.loginTpo(request));
}
@PutMapping("/{tpoId}/approve")
public ResponseEntity<?> approveTpo(@PathVariable Long tpoId, @RequestParam boolean approved) {
    // Call service to update TPO approval status
    boolean result = tpoService.updateApprovalStatus(tpoId, approved);

    if(result) {
        return ResponseEntity.ok("TPO approval status updated.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TPO not found.");
    }
}
@GetMapping("/test")
    public ResponseEntity<String> testProtected() {
        return ResponseEntity.ok("✅ Protected endpoint is working!");
    }

}
