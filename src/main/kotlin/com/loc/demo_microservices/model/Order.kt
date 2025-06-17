package com.loc.demo_microservices.model

import java.math.BigDecimal


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

