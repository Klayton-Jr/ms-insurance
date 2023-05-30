package com.audsat.msinsurance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cars")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Cars {
    @Id
    private Long id;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column(name = "car_year")
    private String year;

    @Column
    private BigDecimal fipeValue;
}
