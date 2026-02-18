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

    private String priority;

    private String riskLevel;

    private Float confidenceScore;

    private OffsetDateTime createdAt;
}