package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface DriversRepository extends JpaRepository<Drivers, Long> {
    @Query("""
            select (count(d) > 0) from Drivers d inner join d.carDriversList carDriversList
            where d.document = :document and d.birthdate = :birthdate and carDriversList.cars.id = :id and carDriversList.mainDriver = false""")
    boolean existsDriverByDocumentAndBirthDateAndCarAntNotMainDriver(@Param("document") @NonNull String document, @Param("birthdate") @NonNull LocalDate birthdate, @Param("id") @NonNull Long id);
    @Query("select d from Drivers d where d.document = ?1 and d.birthdate = ?2")
    Optional<Drivers> findByDocumentAndBirthDate(@NonNull String document, @NonNull LocalDate birthdate);
}