package com.loc.order_service.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    val id: Long? = null,
    val orderNumber: String,
    val skuCode: String,
    val price: BigDecimal,
    val quantity: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
}

