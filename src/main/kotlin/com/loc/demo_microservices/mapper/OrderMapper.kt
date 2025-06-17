package com.loc.demo_microservices.mapper

import com.loc.demo_microservices.entity.OrderEntity
import com.loc.demo_microservices.model.Order

import com.loc.orderservice.model.OrderRequest
import com.loc.orderservice.model.OrderResponse
import java.time.LocalDateTime
import java.util.*

fun OrderRequest.toModel(): Order {
    return Order(
        orderNumber = generateOrderNumber(),
        skuCode = this.skuCode,           
        price = this.price,               
        quantity = this.quantity,         
        createdAt = LocalDateTime.now()
    )
}

fun Order.toResponse(): OrderResponse {
    return OrderResponse(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity
    )
}

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity
    )
}

fun OrderEntity.toModel(): Order {
    return Order(
        id = this.id,
        orderNumber = this.orderNumber,
        skuCode = this.skuCode,
        price = this.price,
        quantity = this.quantity,
        createdAt = LocalDateTime.now() 
    )
}

fun List<OrderEntity>.toModels(): List<Order> = this.map { it.toModel() }
fun List<Order>.toResponses(): List<OrderResponse> = this.map { it.toResponse() }

private fun generateOrderNumber(): String {
    val timestamp = System.currentTimeMillis()
    val random = UUID.randomUUID().toString().substring(0, 8).uppercase()
    return "ORD-$timestamp-$random"
}

fun OrderRequest.toModelWithValidation(): Result<Order> {
    return try {
        // Basic validation
        if (this.price <= java.math.BigDecimal.ZERO) {
            return Result.failure(IllegalArgumentException("Price must be greater than 0"))
        }
        
        if (this.quantity <= 0) {
            return Result.failure(IllegalArgumentException("Quantity must be greater than 0"))
        }
        
        if (this.skuCode.isBlank()) {
            return Result.failure(IllegalArgumentException("SKU Code cannot be blank"))
        }
        
        val order = this.toModel()
        Result.success(order)
    } catch (e: Exception) {
        Result.failure(e)
    }
}