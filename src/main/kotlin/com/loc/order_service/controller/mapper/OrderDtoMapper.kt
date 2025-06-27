package com.loc.order_service.controller.mapper

import com.loc.order_service.domain.model.Order
import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.orderservice.model.OrderRequest
import com.loc.orderservice.model.OrderResponse
import java.util.*

// ✅ Domain ↔ DTO mapping Interface layer (controller)
fun OrderRequest.toModel(): Order {
    return Order(
        orderNumber = generateOrderNumber(),
        skuCode = this.skuCode,           
        price = this.price,               
        quantity = this.quantity,
        status = OrderStatusEnum.PENDING
    )
}

fun Order.toResponse(): OrderResponse {
    return OrderResponse(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity,
        status = this.status.toResponseStatus()
    )
}

fun OrderStatusEnum.toResponseStatus(): OrderResponse.Status {
    return when (this) {
        OrderStatusEnum.PENDING -> OrderResponse.Status.PENDING
        OrderStatusEnum.CONFIRMED -> OrderResponse.Status.CONFIRMED
        OrderStatusEnum.PROCESSING -> OrderResponse.Status.PROCESSING
        OrderStatusEnum.SHIPPED -> OrderResponse.Status.SHIPPED
        OrderStatusEnum.DELIVERED -> OrderResponse.Status.DELIVERED
        OrderStatusEnum.CANCELLED -> OrderResponse.Status.CANCELLED
        OrderStatusEnum.FAILED -> OrderResponse.Status.FAILED
    }
}

fun List<Order>.toResponses(): List<OrderResponse> = this.map { it.toResponse() }

private fun generateOrderNumber(): String {
    val timestamp = System.currentTimeMillis()
    val random = UUID.randomUUID().toString().substring(0, 8).uppercase()
    return "ORD-$timestamp-$random"
} 