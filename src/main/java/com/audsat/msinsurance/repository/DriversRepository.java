package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface DriversRepository extends JpaRepository<Drivers, Long> {
    @Query("select d from Drivers d where d.document = ?1 and d.birthdate = ?2")
    Optional<Drivers> findByDocumentAndBirthDate(@NonNull String document, @NonNull LocalDate birthdate);
}