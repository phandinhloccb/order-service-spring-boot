package com.loc.demo_microservices.service

import com.loc.demo_microservices.entity.OrderEntity
import com.loc.demo_microservices.mapper.*
import com.loc.demo_microservices.model.Order
import com.loc.demo_microservices.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    @Transactional
    fun createOrder(order: Order): Order {
        val saveEntity: OrderEntity = orderRepository.save(order.toEntity())
        return saveEntity.toModel()
    }
}