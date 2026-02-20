package com.etiya.etiyatelekom.entity;

import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agent")
@ToString
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_domain_id")
    private ServiceDomain serviceDomain;

    private String fullName;

    private String email;

    private AgentRoleEnums role;

    private Boolean isActive;
}