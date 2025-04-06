package com.loc.microservices.order_service_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.loc.microservices.order_service_spring_boot.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}