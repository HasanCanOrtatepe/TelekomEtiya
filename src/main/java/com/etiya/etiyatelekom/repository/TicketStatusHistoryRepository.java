package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory,Long> {
    List<TicketStatusHistory> findByTicketId(Long ticketId);

}
