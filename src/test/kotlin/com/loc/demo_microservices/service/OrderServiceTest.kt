package com.loc.order_service.service

import com.loc.order_service.entity.OrderEntity
import com.loc.order_service.model.InventoryResult
import com.loc.order_service.model.Order
import com.loc.order_service.model.OrderResult
import com.loc.order_service.repository.OrderRepository
import com.loc.order_service.service.client.InventoryService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
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

    @MockK
    private lateinit var inventoryService: InventoryService

    private val orderService: OrderService by lazy {
        OrderService(
            orderRepository = orderRepository,
            inventoryService = inventoryService
        )
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
    fun `should create order successfully when inventory is in stock`() = runTest {
        // Given
        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY)
        } returns InventoryResult.InStock

        every {
            orderRepository.save(sampleOrderEntity)
        } returns savedOrderEntity

        // When
        val actualResult = orderService.createOrder(sampleOrder)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY) }
        verify { orderRepository.save(sampleOrderEntity) }

        assertThat(actualResult).isInstanceOf(OrderResult.Success::class.java)
        val successResult = actualResult as OrderResult.Success
        assertThat(successResult.order.id).isEqualTo(1L)
        assertThat(successResult.order.orderNumber).isEqualTo(SAMPLE_ORDER_NUMBER)
        assertThat(successResult.order.skuCode).isEqualTo(SAMPLE_SKU_CODE)
        assertThat(successResult.order.price).isEqualTo(SAMPLE_PRICE)
        assertThat(successResult.order.quantity).isEqualTo(SAMPLE_QUANTITY)
        assertThat(successResult.order.createdAt).isNotNull()
    }

    @Test
    fun `should return business failure when inventory is out of stock`() = runTest {
        // Given
        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY)
        } returns InventoryResult.OutOfStock

        // When
        val actualResult = orderService.createOrder(sampleOrder)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY) }
        verify(exactly = 0) { orderRepository.save(any()) }

        assertThat(actualResult).isInstanceOf(OrderResult.BusinessFailure::class.java)
        val failureResult = actualResult as OrderResult.BusinessFailure
        assertThat(failureResult.reason).isEqualTo("Product $SAMPLE_SKU_CODE is out of stock")
    }

    @Test
    fun `should throw exception when database constraint violation occurs after inventory check`() = runTest {
        // Given
        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY)
        } returns InventoryResult.InStock

        every {
            orderRepository.save(sampleOrderEntity)
        } throws DataIntegrityViolationException("Duplicate order number")

        // When & Then
        org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
            runTest {
                orderService.createOrder(sampleOrder)
            }
        }

        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY) }
        verify { orderRepository.save(sampleOrderEntity) }
    }

    @Test
    fun `should handle order with different quantities when in stock`() = runTest {
        // Given
        val orderWithDifferentQuantity = sampleOrder.copy(quantity = 5)
        val entityWithDifferentQuantity = sampleOrderEntity.copy(quantity = 5)
        val savedEntityWithDifferentQuantity = savedOrderEntity.copy(quantity = 5)

        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, 5)
        } returns InventoryResult.InStock

        every {
            orderRepository.save(entityWithDifferentQuantity)
        } returns savedEntityWithDifferentQuantity

        // When
        val actualResult = orderService.createOrder(orderWithDifferentQuantity)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, 5) }
        verify { orderRepository.save(entityWithDifferentQuantity) }

        assertThat(actualResult).isInstanceOf(OrderResult.Success::class.java)
        val successResult = actualResult as OrderResult.Success
        assertThat(successResult.order.quantity).isEqualTo(5)
    }

    @Test
    fun `should handle order with different price when in stock`() = runTest {
        // Given
        val differentPrice = BigDecimal("1299.99")
        val orderWithDifferentPrice = sampleOrder.copy(price = differentPrice)
        val entityWithDifferentPrice = sampleOrderEntity.copy(price = differentPrice)
        val savedEntityWithDifferentPrice = savedOrderEntity.copy(price = differentPrice)

        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY)
        } returns InventoryResult.InStock

        every {
            orderRepository.save(entityWithDifferentPrice)
        } returns savedEntityWithDifferentPrice

        // When
        val actualResult = orderService.createOrder(orderWithDifferentPrice)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY) }
        verify { orderRepository.save(entityWithDifferentPrice) }

        assertThat(actualResult).isInstanceOf(OrderResult.Success::class.java)
        val successResult = actualResult as OrderResult.Success
        assertThat(successResult.order.price).isEqualTo(differentPrice)
    }
}