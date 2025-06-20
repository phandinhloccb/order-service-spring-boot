package com.loc.order_service.service

import com.loc.order_service.entity.OrderEntity
import com.loc.order_service.model.Order
import com.loc.order_service.repository.OrderRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal
import java.time.LocalDateTime

private const val SAMPLE_ORDER_NUMBER = "ORD-1234567890-ABCD1234"
private const val SAMPLE_SKU_CODE = "IPHONE13-128"
private val SAMPLE_PRICE = BigDecimal("999.99")
private const val SAMPLE_QUANTITY = 2

@ExtendWith(MockKExtension::class)
class OrderServiceTest {

    @MockK
    private lateinit var orderRepository: OrderRepository

    private val orderService: OrderService by lazy {
        OrderService(orderRepository = orderRepository)
    }

    @BeforeEach
    fun clearMocks() {
        clearAllMocks(answers = false)
    }

    private val sampleOrder = Order(
        id = null,
        orderNumber = SAMPLE_ORDER_NUMBER,
        skuCode = SAMPLE_SKU_CODE,
        price = SAMPLE_PRICE,
        quantity = SAMPLE_QUANTITY,
        createdAt = LocalDateTime.now()
    )

    private val sampleOrderEntity = OrderEntity(
        id = null,
        orderNumber = SAMPLE_ORDER_NUMBER,
        skuCode = SAMPLE_SKU_CODE,
        price = SAMPLE_PRICE,
        quantity = SAMPLE_QUANTITY
    )

    private val savedOrderEntity = OrderEntity(
        id = 1L,
        orderNumber = SAMPLE_ORDER_NUMBER,
        skuCode = SAMPLE_SKU_CODE,
        price = SAMPLE_PRICE,
        quantity = SAMPLE_QUANTITY
    )

    @Test
    fun `should create order successfully`() {
        // Given
        every {
            orderRepository.save(sampleOrderEntity)
        } returns savedOrderEntity

        // When
        val actualResult = orderService.createOrder(sampleOrder)

        // Then
        verify { orderRepository.save(sampleOrderEntity) }
        assertThat(actualResult.id).isEqualTo(1L)
        assertThat(actualResult.orderNumber).isEqualTo(SAMPLE_ORDER_NUMBER)
        assertThat(actualResult.skuCode).isEqualTo(SAMPLE_SKU_CODE)
        assertThat(actualResult.price).isEqualTo(SAMPLE_PRICE)
        assertThat(actualResult.quantity).isEqualTo(SAMPLE_QUANTITY)
        assertThat(actualResult.createdAt).isNotNull()
    }

    @Test
    fun `should throw exception when database constraint violation occurs`() {
        // Given
        every {
            orderRepository.save(sampleOrderEntity)
        } throws DataIntegrityViolationException("Duplicate order number")

        // When & Then
        org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
            orderService.createOrder(sampleOrder)
        }

        verify { orderRepository.save(sampleOrderEntity) }
    }

    @Test
    fun `should handle order with different quantities`() {
        // Given
        val orderWithDifferentQuantity = sampleOrder.copy(quantity = 5)
        val entityWithDifferentQuantity = sampleOrderEntity.copy(quantity = 5)
        val savedEntityWithDifferentQuantity = savedOrderEntity.copy(quantity = 5)

        every {
            orderRepository.save(entityWithDifferentQuantity)
        } returns savedEntityWithDifferentQuantity

        // When
        val actualResult = orderService.createOrder(orderWithDifferentQuantity)

        // Then
        verify { orderRepository.save(entityWithDifferentQuantity) }
        assertThat(actualResult.quantity).isEqualTo(5)
    }

    @Test
    fun `should handle order with different price`() {
        // Given
        val differentPrice = BigDecimal("1299.99")
        val orderWithDifferentPrice = sampleOrder.copy(price = differentPrice)
        val entityWithDifferentPrice = sampleOrderEntity.copy(price = differentPrice)
        val savedEntityWithDifferentPrice = savedOrderEntity.copy(price = differentPrice)

        every {
            orderRepository.save(entityWithDifferentPrice)
        } returns savedEntityWithDifferentPrice

        // When
        val actualResult = orderService.createOrder(orderWithDifferentPrice)

        // Then
        verify { orderRepository.save(entityWithDifferentPrice) }
        assertThat(actualResult.price).isEqualTo(differentPrice)
    }
}