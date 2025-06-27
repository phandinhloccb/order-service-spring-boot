package com.loc.order_service.application.port.repository

import com.loc.order_service.domain.model.Order

interface OrderDomainRepository {
    fun save(order: Order): Order
}