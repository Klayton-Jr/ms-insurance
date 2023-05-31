package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Claims;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimsRepository extends JpaRepository<Claims, Long> {
}