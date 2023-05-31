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
    private Long id;

    @Column
    @Setter
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Drivers drivers;

    @OneToMany(mappedBy = "customer")
    private List<Insurances> insurancesList;
}
