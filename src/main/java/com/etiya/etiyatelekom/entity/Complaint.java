package com.etiya.etiyatelekom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Column(length = 250)
    private String title;

    @Column(length = 5000)
    private String description;

    private OffsetDateTime createdAt;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private AIAnalysis aiAnalysis;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private Ticket ticket;
}