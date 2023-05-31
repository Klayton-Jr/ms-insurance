package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Setter
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", nullable = false)
    private Drivers drivers;

    @OneToMany(mappedBy = "customer")
    private List<Insurances> insurancesList;
}
