package com.etiya.etiyatelekom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerNo;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String password;

    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CustomerSubscription> customerSubscriptions;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Complaint> complaints;
}