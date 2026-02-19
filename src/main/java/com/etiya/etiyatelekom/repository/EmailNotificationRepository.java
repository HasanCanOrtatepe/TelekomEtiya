package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification,Long> {

    List<EmailNotification> findByTicket_Id(Long ticketId);

    List<EmailNotification> findByStatus(String status);

    boolean existsByStatus(String status);

    boolean existsByTicket_Id(Long ticketId);


}
