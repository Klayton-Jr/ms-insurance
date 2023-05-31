package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepository extends JpaRepository<Cars, Long> {
}