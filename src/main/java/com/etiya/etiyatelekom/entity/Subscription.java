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

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<CustomerSubscription> customerSubscriptions;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_domain_id")
    private ServiceDomain serviceDomain;

    private String packageName;

    private LocalDate createDate;

    private Integer durationDays;

    private Double price;

}