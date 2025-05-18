package com.example.demo.repository;

import com.example.demo.entity.Tpo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TpoRepository extends JpaRepository<Tpo, Long> {
    Optional<Tpo> findByCollegeEmail(String collegeEmail);
}
