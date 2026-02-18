package com.etiya.etiyatelekom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String serviceType;

    private String packageName;

    private String status;

    private LocalDate activationDate;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<Complaint> complaints;
}