package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.AIAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIAnalysisRepository extends JpaRepository<AIAnalysis,Long> {

    AIAnalysis findByComplaintId(Long complaintId);
    boolean existsByComplaintId(Long complaintId);

}
