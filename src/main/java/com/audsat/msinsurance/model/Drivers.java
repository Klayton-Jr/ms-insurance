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

    @OneToOne(mappedBy = "drivers", cascade = CascadeType.ALL)
    @Setter
    private Customer customer;

    @OneToMany(mappedBy = "drivers")
    private List<CarDrivers> carDriversList;
}
