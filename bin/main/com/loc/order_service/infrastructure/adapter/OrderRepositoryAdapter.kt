package com.loc.order_service.infrastructure.adapter   

import com.loc.order_service.application.port.repository.OrderDomainRepository
import com.loc.order_service.domain.model.Order
import com.loc.order_service.infrastructure.repository.OrderRepository
import com.loc.order_service.infrastructure.mapper.toEntity
import com.loc.order_service.infrastructure.mapper.toModel
import org.springframework.stereotype.Component

@Component
class OrderRepositoryAdapter(
    private val orderJpaRepository: OrderRepository
): OrderDomainRepository {

    override fun save(order: Order): Order {
        val savedEntity = orderJpaRepository.save(order.toEntity())
        return savedEntity.toModel()
    }
} 