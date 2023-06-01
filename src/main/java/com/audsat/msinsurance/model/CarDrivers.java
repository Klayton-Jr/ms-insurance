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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "driver_id", nullable = false)
    private Drivers drivers;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Cars cars;

    @Column(name = "is_main_driver")
    private Boolean mainDriver;

    @OneToMany(mappedBy = "carDrivers")
    private List<Claims> claimsList;
}
