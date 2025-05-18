package com.example.demo.service;

import com.example.demo.dto.TpoLoginRequest;
import com.example.demo.dto.TpoLoginResponse;
import com.example.demo.dto.TpoRegisterRequest;
import com.example.demo.entity.Tpo;
import com.example.demo.repository.TpoRepository;
import com.example.demo.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TpoService {

    private final TpoRepository tpoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String registerTpo(TpoRegisterRequest request) {
        if (tpoRepository.findByCollegeEmail(request.getCollegeEmail()).isPresent()) {
            return "Email already registered";
        }

        Tpo tpo = Tpo.builder()
                .name(request.getName())
                .idNumber(request.getIdNumber())
                .designation(request.getDesignation())
                .campus(request.getCampus())
                .phone(request.getPhone())
                .collegeEmail(request.getCollegeEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isApproved(false) // default not approved
                .build();

        tpoRepository.save(tpo);
        return "TPO registration successful. Waiting for admin approval.";
    }

    public TpoLoginResponse loginTpo(TpoLoginRequest request) {
    Optional<Tpo> optionalTpo = tpoRepository.findByCollegeEmail(request.getCollegeEmail());

    if (optionalTpo.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    Tpo tpo = optionalTpo.get();

    if (!tpo.isApproved()) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account not approved by admin");
    }

    if (!passwordEncoder.matches(request.getPassword(), tpo.getPassword())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    String token = jwtUtil.generateToken(tpo.getCollegeEmail());

    return new TpoLoginResponse(token, "Login successful");
}

public boolean updateApprovalStatus(Long tpoId, boolean approved) {
    Optional<Tpo> optionalTpo = tpoRepository.findById(tpoId);
    if(optionalTpo.isPresent()) {
        Tpo tpo = optionalTpo.get();
        tpo.setApproved(approved); // or tpo.setStatus(approved ? "approved" : "rejected");
        tpoRepository.save(tpo);
        return true;
    }
    return false;
}
public List<Tpo> getAllTpos() {
    return tpoRepository.findAll();
}

}
