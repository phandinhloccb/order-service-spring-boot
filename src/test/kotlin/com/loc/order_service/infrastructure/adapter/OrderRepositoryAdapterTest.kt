package com.loc.order_service.infrastructure.adapter

import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.Order
import com.loc.order_service.infrastructure.entity.OrderEntity
import com.loc.order_service.infrastructure.repository.OrderRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class OrderRepositoryAdapterTest {

    @MockK
    private lateinit var orderJpaRepository: OrderRepository

    private val orderRepositoryAdapter by lazy {
        OrderRepositoryAdapter(orderJpaRepository)
    }

    @Test
    fun `should save order and return domain model`() {
        // Given
        val order = Order(
            id = null,
            orderNumber = "ORD-123",
            skuCode = "SKU-123",
            price = BigDecimal("100.00"),
            quantity = 1,
            status = OrderStatusEnum.PENDING
        )

        val entity = OrderEntity(
            id = null,
            orderNumber = "ORD-123",
            skuCode = "SKU-123",
            price = BigDecimal("100.00"),
            quantity = 1,
            status = OrderStatusEnum.PENDING
        )

        val savedEntity = OrderEntity(
            id = 1L,
            orderNumber = "ORD-123",
            skuCode = "SKU-123",
            price = BigDecimal("100.00"),
            quantity = 1,
            status = OrderStatusEnum.PENDING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { orderJpaRepository.save(any<OrderEntity>()) } returns savedEntity

        // When
        val result = orderRepositoryAdapter.save(order)

        // Then
        verify { orderJpaRepository.save(any<OrderEntity>()) }
        
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.orderNumber).isEqualTo("ORD-123")
        assertThat(result.skuCode).isEqualTo("SKU-123")
        assertThat(result.createdAt).isNotNull()
        assertThat(result.updatedAt).isNotNull()
    }
} 