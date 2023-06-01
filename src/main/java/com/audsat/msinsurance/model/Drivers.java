package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String document;

    @Column
    private LocalDate birthdate;

    @OneToMany(mappedBy = "drivers", cascade = CascadeType.PERSIST)
    private List<Customer> customer;

    @OneToMany(mappedBy = "drivers", cascade = CascadeType.PERSIST)
    private List<CarDrivers> carDriversList;
}
