package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Insurances;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancesRepository extends JpaRepository<Insurances, Long> {
}