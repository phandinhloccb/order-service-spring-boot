package com.loc.order_service.application.service

import com.loc.order_service.application.port.repository.OrderDomainRepository
import com.loc.order_service.application.service.client.InventoryService
import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.InventoryResult
import com.loc.order_service.domain.model.Order
import com.loc.order_service.domain.model.OrderResult
import com.loc.order_service.infrastructure.producer.KafkaEventProducer
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDateTime

private const val SAMPLE_ORDER_NUMBER = "ORD-1234567890-ABCD1234"
private const val SAMPLE_SKU_CODE = "IPHONE13-128"
private val SAMPLE_PRICE = BigDecimal("999.99")
private const val SAMPLE_QUANTITY = 2

@ExtendWith(MockKExtension::class)
class OrderServiceTest {

    @MockK
    private lateinit var orderDomainRepository: OrderDomainRepository

    @MockK
    private lateinit var inventoryService: InventoryService

    @MockK
    private lateinit var kafkaEventProducer: KafkaEventProducer

    private val orderService: OrderService by lazy {
        OrderService(
            orderDomainRepository = orderDomainRepository,
            inventoryService = inventoryService,
            kafkaEventProducer = kafkaEventProducer
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
        status = OrderStatusEnum.PENDING,
        createdAt = LocalDateTime.now()
    )

    private val savedOrder = Order(
        id = 1L,
        orderNumber = SAMPLE_ORDER_NUMBER,
        skuCode = SAMPLE_SKU_CODE,
        price = SAMPLE_PRICE,
        quantity = SAMPLE_QUANTITY,
        status = OrderStatusEnum.CONFIRMED,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `should create order successfully when inventory is in stock`() = runTest {
        // Given
        val orderWithConfirmedStatus = sampleOrder.copy(status = OrderStatusEnum.CONFIRMED)
        
        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY)
        } returns InventoryResult.InStock

        coEvery {
            orderDomainRepository.save(orderWithConfirmedStatus)
        } returns savedOrder

        coEvery {
            kafkaEventProducer.publishOrderCompletedEvent(any())
        } returns Unit

        // When
        val actualResult = orderService.createOrder(sampleOrder)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, SAMPLE_QUANTITY) }
        coVerify { orderDomainRepository.save(orderWithConfirmedStatus) }
        coVerify { kafkaEventProducer.publishOrderCompletedEvent(any()) }

        assertThat(actualResult).isInstanceOf(OrderResult.Success::class.java)
        val successResult = actualResult as OrderResult.Success
        assertThat(successResult.order.id).isEqualTo(1L)
        assertThat(successResult.order.orderNumber).isEqualTo(SAMPLE_ORDER_NUMBER)
        assertThat(successResult.order.skuCode).isEqualTo(SAMPLE_SKU_CODE)
        assertThat(successResult.order.status).isEqualTo(OrderStatusEnum.CONFIRMED)
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
        coVerify(exactly = 0) { orderDomainRepository.save(any()) }
        coVerify(exactly = 0) { kafkaEventProducer.publishOrderCompletedEvent(any()) }

        assertThat(actualResult).isInstanceOf(OrderResult.BusinessFailure::class.java)
        val failureResult = actualResult as OrderResult.BusinessFailure
        assertThat(failureResult.reason).isEqualTo("Product $SAMPLE_SKU_CODE is out of stock")
    }

    @Test
    fun `should handle order with different quantities when in stock`() = runTest {
        // Given
        val orderWithDifferentQuantity = sampleOrder.copy(quantity = 5)
        val orderWithConfirmedStatus = orderWithDifferentQuantity.copy(status = OrderStatusEnum.CONFIRMED)
        val savedOrderWithDifferentQuantity = savedOrder.copy(quantity = 5)

        coEvery {
            inventoryService.checkStock(SAMPLE_SKU_CODE, 5)
        } returns InventoryResult.InStock

        coEvery {
            orderDomainRepository.save(orderWithConfirmedStatus)
        } returns savedOrderWithDifferentQuantity

        coEvery {
            kafkaEventProducer.publishOrderCompletedEvent(any())
        } returns Unit

        // When
        val actualResult = orderService.createOrder(orderWithDifferentQuantity)

        // Then
        coVerify { inventoryService.checkStock(SAMPLE_SKU_CODE, 5) }
        coVerify { orderDomainRepository.save(orderWithConfirmedStatus) }

        assertThat(actualResult).isInstanceOf(OrderResult.Success::class.java)
        val successResult = actualResult as OrderResult.Success
        assertThat(successResult.order.quantity).isEqualTo(5)
    }
}