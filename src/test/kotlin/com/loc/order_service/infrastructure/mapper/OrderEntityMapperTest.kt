package com.loc.order_service.infrastructure.mapper

import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.Order
import com.loc.order_service.infrastructure.entity.OrderEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderEntityMapperTest {

    @Test
    fun `should map Order to OrderEntity`() {
        // Given
        val order = Order(
            id = 1L,
            orderNumber = "ORD-123",
            skuCode = "SKU-123",
            price = BigDecimal("100.00"),
            quantity = 1,
            status = OrderStatusEnum.CONFIRMED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When
        val result = order.toEntity()

        // Then
        assertThat(result.id).isEqualTo(order.id)
        assertThat(result.orderNumber).isEqualTo(order.orderNumber)
        assertThat(result.skuCode).isEqualTo(order.skuCode)
        assertThat(result.price).isEqualTo(order.price)
        assertThat(result.quantity).isEqualTo(order.quantity)
        assertThat(result.status).isEqualTo(order.status)
    }

    @Test
    fun `should map OrderEntity to Order`() {
        // Given
        val entity = OrderEntity(
            id = 1L,
            orderNumber = "ORD-123",
            skuCode = "SKU-123",
            price = BigDecimal("100.00"),
            quantity = 1,
            status = OrderStatusEnum.CONFIRMED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When
        val result = entity.toModel()

        // Then
        assertThat(result.id).isEqualTo(entity.id)
        assertThat(result.orderNumber).isEqualTo(entity.orderNumber)
        assertThat(result.skuCode).isEqualTo(entity.skuCode)
        assertThat(result.price).isEqualTo(entity.price)
        assertThat(result.quantity).isEqualTo(entity.quantity)
        assertThat(result.status).isEqualTo(entity.status)
        assertThat(result.createdAt).isEqualTo(entity.createdAt)
        assertThat(result.updatedAt).isEqualTo(entity.updatedAt)
    }
} 