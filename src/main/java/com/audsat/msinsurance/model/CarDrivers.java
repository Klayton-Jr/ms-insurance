package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "car_drivers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CarDrivers {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Drivers drivers;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Cars cars;

    @Column(name = "is_main_driver")
    private Boolean mainDriver;
}
