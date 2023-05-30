package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "claims")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Claims {
    @Id
    private Long id;

    @OneToMany
    @JoinColumn(name = "car_id")
    private List<Cars> cars;

    @OneToMany
    @JoinColumn(name = "driver_id")
    private List<CarDrivers> carDrivers;

    @Column
    private LocalDate eventDate;
}
