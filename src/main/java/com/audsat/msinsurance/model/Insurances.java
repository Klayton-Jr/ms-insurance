package com.audsat.msinsurance.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "insurances")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Insurances {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    @CreatedDate
    private LocalDateTime creationDt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean active;
}
