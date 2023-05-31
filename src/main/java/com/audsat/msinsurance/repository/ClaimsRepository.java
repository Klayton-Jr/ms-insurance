package com.audsat.msinsurance.repository;

import com.audsat.msinsurance.model.Claims;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClaimsRepository extends JpaRepository<Claims, Long> {
    @Query("SELECT count(claim) > 0 FROM Claims claim " +
            "JOIN claim.carDrivers carDriver " +
            "JOIN carDriver.drivers driver " +
            "WHERE driver.document = :driverDocument")
    boolean existsClaimByDriverDocument(String driverDocument);

    @Query("select (count(c) > 0) from Claims c where c.id is not null and c.cars.id = :carId")
    boolean existsClaimsByCarId(Long carId);
}