package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurances")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Insurances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Cars cars;

    @Column(name = "is_active")
    @Setter
    private Boolean active;

    @Column
    private BigDecimal amount;
}
