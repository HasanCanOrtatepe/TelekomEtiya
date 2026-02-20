package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Complaint;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.entity.CustomerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription,Long> {

    boolean existsByCustomerIdAndSubscriptionId(Long customerId, Long subscriptionId);

    List<CustomerSubscription> findByCustomerId(Long customerId);
    boolean existsByCustomerId(Long customerId);

    List<CustomerSubscription> findByCustomerIdAndStatus(Long customerId, String status);
    boolean existsByCustomerIdAndStatus(Long customerId, String status);


}
