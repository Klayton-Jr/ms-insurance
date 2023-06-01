package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cars")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column(name = "car_year")
    private String year;

    @Column
    private BigDecimal fipeValue;

    @OneToMany(mappedBy = "cars")
    private List<Claims> claimsList;

    @OneToMany(mappedBy = "cars", cascade = CascadeType.PERSIST)
    private List<Insurances> insurancesList;

    @OneToMany(mappedBy = "cars", cascade = CascadeType.ALL)
    private List<CarDrivers> carDriversList;
}
