package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

}
