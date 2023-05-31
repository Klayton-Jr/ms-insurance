package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface DriversRepository extends JpaRepository<Drivers, Long> {
    @Query("select d from Drivers d where d.document = ?1")
    Optional<Drivers> findByDocument(@NonNull String document);
}