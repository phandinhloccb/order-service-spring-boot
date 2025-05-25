package com.loc.microservices.order_service_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.loc.microservices.order_service_spring_boot.model.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
}