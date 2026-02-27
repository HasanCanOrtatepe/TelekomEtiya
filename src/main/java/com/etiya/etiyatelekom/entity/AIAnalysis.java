package com.etiya.etiyatelekom.entity;

import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ai_analysis")
public class AIAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "complaint_id", unique = true)
    private Complaint complaint;

    @Column(columnDefinition = "text")
    private String summary;

    @Enumerated(EnumType.STRING)
    private TicketPriorityEnums priority;

    @Enumerated(EnumType.STRING)
    private TicketRiskLevelEnums riskLevel;

    private Float confidenceScore;

    private OffsetDateTime createdAt;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;
}