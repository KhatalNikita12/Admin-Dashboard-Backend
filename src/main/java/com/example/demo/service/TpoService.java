package com.example.demo.service;

import com.example.demo.dto.TpoLoginRequest;
import com.example.demo.dto.TpoLoginResponse;
import com.example.demo.dto.TpoRegisterRequest;
import com.example.demo.entity.Tpo;
import com.example.demo.repository.TpoRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TpoService {
    @Autowired
    private final TpoRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void registerTpo(TpoRegisterRequest req) {
        if (repo.findByCollegeEmail(req.getCollegeEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        Tpo t = Tpo.builder()
                .name(req.getName())
                .idNumber(req.getIdNumber())
                .designation(req.getDesignation())
                .campus(req.getCampus())
                .phone(req.getPhone())
                .collegeEmail(req.getCollegeEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status("pending")
                .build();
        repo.save(t);
    }

    public TpoLoginResponse loginTpo(TpoLoginRequest req) {
        Tpo t = repo.findByCollegeEmail(req.getCollegeEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!"approved".equalsIgnoreCase(t.getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account not approved by admin");
        }
        if (!passwordEncoder.matches(req.getPassword(), t.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        String token = jwtUtil.generateToken(t.getCollegeEmail());
        return new TpoLoginResponse(token, "Login successful");
    }

    public void updateApprovalStatus(Long id, boolean approved) {
        Tpo t = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TPO not found"));
        t.setStatus(approved ? "approved" : "rejected");
        repo.save(t);
    }

    public Page<Tpo> getAllTpos(int page, int size) {
        Pageable pg = PageRequest.of(page, size, Sort.by("id").ascending());
        return repo.findAll(pg);
    }

    public void updateTpoDetails(Long id, String name, String campus) {
        Tpo t = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TPO not found"));
        t.setName(name);
        t.setCampus(campus);
        repo.save(t);
    }

    public void deleteTpo(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TPO not found");
        }
        repo.deleteById(id);
    }
    public List<Tpo> getTposByStatus(String status) {
    return repo.findByStatus(status);
}
public long countAll() {
    return repo.count();
}

public long countByStatus(String status) {
    return repo.countByStatus(status);
}

}
