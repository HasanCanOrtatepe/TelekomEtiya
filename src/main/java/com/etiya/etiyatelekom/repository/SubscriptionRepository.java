package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByCustomer_Id(Long customerId);

    List<Subscription> findByStatus(String status);

    boolean existsByStatus(String email);
    boolean existsByCustomerId(Long customerId);



}
