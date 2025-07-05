package com.loc.order_service.controller.mapper

import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.Order
import com.loc.orderservice.model.OrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderDtoMapperTest {

    @Test
    fun `should map OrderRequest to Order`() {
        // Given
        val orderRequest = OrderRequest(
            skuCode = "IPHONE13-128",
            price = BigDecimal("999.99"),
            quantity = 2
        )

        // When
        val result = orderRequest.toModel()

        // Then
        assertThat(result.id).isNull()
        assertThat(result.orderNumber).isNotBlank()
        assertThat(result.orderNumber).startsWith("ORD-")
        assertThat(result.skuCode).isEqualTo(orderRequest.skuCode)
        assertThat(result.price).isEqualTo(orderRequest.price)
        assertThat(result.quantity).isEqualTo(orderRequest.quantity)
        assertThat(result.status).isEqualTo(OrderStatusEnum.PENDING)
    }

    @Test
    fun `should map Order to OrderResponse`() {
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
        val result = order.toResponse()

        // Then
        assertThat(result.id).isEqualTo(order.id)
        assertThat(result.orderNumber).isEqualTo(order.orderNumber)
        assertThat(result.skuCode).isEqualTo(order.skuCode)
        assertThat(result.price).isEqualTo(order.price)
        assertThat(result.quantity).isEqualTo(order.quantity)
    }
} 