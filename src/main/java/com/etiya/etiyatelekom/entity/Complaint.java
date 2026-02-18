package com.etiya.etiyatelekom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private String title;

    private String description;

    private OffsetDateTime createdAt;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private AIAnalysis aiAnalysis;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private Ticket ticket;
}