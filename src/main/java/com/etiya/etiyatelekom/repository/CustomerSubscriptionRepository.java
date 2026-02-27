package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.common.enums.SubscriptionStatus;

import com.etiya.etiyatelekom.entity.CustomerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription,Long> {

    boolean existsByCustomerIdAndSubscriptionId(Long customerId, Long subscriptionId);

    Optional<CustomerSubscription> findByCustomerIdAndSubscriptionId(Long customerId, Long subscriptionId);

    List<CustomerSubscription> findByCustomerId(Long customerId);

    List<CustomerSubscription> findByCustomerIdAndStatus(Long customerId, SubscriptionStatus subscriptionStatus);


}
