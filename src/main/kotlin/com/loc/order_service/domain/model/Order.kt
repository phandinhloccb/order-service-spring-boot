package com.loc.order_service.domain.model

import com.loc.order_service.domain.enum.OrderStatusEnum
import java.math.BigDecimal
import java.time.LocalDateTime

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

