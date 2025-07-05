package com.loc.order_service.infrastructure.mapper

import com.loc.order_service.infrastructure.entity.OrderEntity
import com.loc.order_service.domain.model.Order

// ✅ Domain ↔ Entity mapping Infrastructure layer
fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity,
        status = this.status
    )
}

fun OrderEntity.toModel(): Order {
    return Order(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<OrderEntity>.toModels(): List<Order> = this.map { it.toModel() } 