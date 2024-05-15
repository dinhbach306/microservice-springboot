package com.dinh.microservices.orderservice.repository;

import com.dinh.microservices.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOderRepository extends JpaRepository<Order, Long> {
}
