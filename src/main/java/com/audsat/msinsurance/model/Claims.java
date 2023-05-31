package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "claims")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Claims {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Cars cars;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private CarDrivers carDrivers;

    @Column
    private LocalDate eventDate;
}
