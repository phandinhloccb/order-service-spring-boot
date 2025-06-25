package com.loc.order_service.model

import java.math.BigDecimal
import java.time.LocalDateTime
import com.loc.order_service.enum.OrderStatusEnum

data class Order(
    val id: Long? = null,
    val orderNumber: String,
    val skuCode: String,
    val price: BigDecimal,
    val quantity: Int,
    val status: OrderStatusEnum,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
}

