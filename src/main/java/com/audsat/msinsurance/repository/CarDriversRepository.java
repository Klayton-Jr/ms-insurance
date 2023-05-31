package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.CarDrivers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDriversRepository extends JpaRepository<CarDrivers, Long> {
}