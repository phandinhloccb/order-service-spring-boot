package com.loc.order_service.infrastructure.repository

import com.loc.order_service.infrastructure.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<OrderEntity, Long> {}