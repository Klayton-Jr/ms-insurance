package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "drivers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Drivers {
    @Id
    private Long id;

    @Column
    private String document;

    @Column
    private LocalDate birthdate;

    @OneToOne(mappedBy = "drivers")
    private Customer customer;

    @OneToMany(mappedBy = "drivers")
    private List<CarDrivers> carDrivers;
}
