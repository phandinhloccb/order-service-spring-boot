package com.loc.order_service.infrastructure.entity

import com.loc.order_service.domain.enum.OrderStatusEnum
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name="t_orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val orderNumber: String,

    val skuCode: String,

    val quantity: Int,

    val price: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: OrderStatusEnum,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)