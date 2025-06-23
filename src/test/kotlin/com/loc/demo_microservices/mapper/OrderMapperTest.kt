package com.loc.order_service.mapper

import com.loc.order_service.entity.OrderEntity
import com.loc.order_service.model.Order
import com.loc.orderservice.model.OrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.LocalDateTime

private const val SAMPLE_SKU_CODE = "IPHONE13-128"
private val SAMPLE_PRICE = BigDecimal("999.99")
private const val SAMPLE_QUANTITY = 2
private const val SAMPLE_ORDER_NUMBER = "ORD-1234567890-ABCD1234"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OrderMapperTest {

    @ParameterizedTest
    @MethodSource("orderRequestTestData")
    fun `should map OrderRequest to Order`(
        skuCode: String,
        price: BigDecimal,
        quantity: Int
    ) {
        // Given
        val orderRequest = OrderRequest(
            skuCode = skuCode,
            price = price,
            quantity = quantity
        )

        // When
        val result = orderRequest.toModel()

        // Then
        result.also {
            assertThat(it.id).isNull()
            assertThat(it.orderNumber).isNotBlank()
            assertThat(it.orderNumber).startsWith("ORD-")
            assertThat(it.skuCode).isEqualTo(orderRequest.skuCode)
            assertThat(it.price).isEqualTo(orderRequest.price)
            assertThat(it.quantity).isEqualTo(orderRequest.quantity)
            assertThat(it.createdAt).isNotNull()
            assertThat(it.updatedAt).isNull()
        }
    }

    @Test
    fun `should generate unique order numbers for multiple requests`() {
        // Given
        val orderRequest = OrderRequest(
            skuCode = SAMPLE_SKU_CODE,
            price = SAMPLE_PRICE,
            quantity = SAMPLE_QUANTITY
        )

        // When
        val order1 = orderRequest.toModel()
        val order2 = orderRequest.toModel()

        // Then
        assertThat(order1.orderNumber).isNotEqualTo(order2.orderNumber)
        assertThat(order1.orderNumber).startsWith("ORD-")
        assertThat(order2.orderNumber).startsWith("ORD-")
    }

    @ParameterizedTest
    @MethodSource("orderToResponseTestData")
    fun `should map Order to OrderResponse`(
        id: Long?,
        orderNumber: String,
        skuCode: String,
        price: BigDecimal,
        quantity: Int
    ) {
        // Given
        val order = Order(
            id = id,
            orderNumber = orderNumber,
            skuCode = skuCode,
            price = price,
            quantity = quantity,
            createdAt = LocalDateTime.now()
        )

        // When
        val result = order.toResponse()

        // Then
        result.also {
            assertThat(it.id).isEqualTo(order.id)
            assertThat(it.orderNumber).isEqualTo(order.orderNumber)
            assertThat(it.skuCode).isEqualTo(order.skuCode)
            assertThat(it.price).isEqualTo(order.price)
            assertThat(it.quantity).isEqualTo(order.quantity)
        }
    }

    @ParameterizedTest
    @MethodSource("orderToEntityTestData")
    fun `should map Order to OrderEntity`(
        id: Long?,
        orderNumber: String,
        skuCode: String,
        price: BigDecimal,
        quantity: Int
    ) {
        // Given
        val order = Order(
            id = id,
            orderNumber = orderNumber,
            skuCode = skuCode,
            price = price,
            quantity = quantity,
            createdAt = LocalDateTime.now()
        )

        // When
        val result = order.toEntity()

        // Then
        result.also {
            assertThat(it.id).isEqualTo(order.id)
            assertThat(it.orderNumber).isEqualTo(order.orderNumber)
            assertThat(it.skuCode).isEqualTo(order.skuCode)
            assertThat(it.price).isEqualTo(order.price)
            assertThat(it.quantity).isEqualTo(order.quantity)
        }
    }

    @ParameterizedTest
    @MethodSource("entityToModelTestData")
    fun `should map OrderEntity to Order`(
        id: Long?,
        orderNumber: String,
        skuCode: String,
        price: BigDecimal,
        quantity: Int
    ) {
        // Given
        val orderEntity = OrderEntity(
            id = id,
            orderNumber = orderNumber,
            skuCode = skuCode,
            price = price,
            quantity = quantity
        )

        // When
        val result = orderEntity.toModel()

        // Then
        result.also {
            assertThat(it.id).isEqualTo(orderEntity.id)
            assertThat(it.orderNumber).isEqualTo(orderEntity.orderNumber)
            assertThat(it.skuCode).isEqualTo(orderEntity.skuCode)
            assertThat(it.price).isEqualTo(orderEntity.price)
            assertThat(it.quantity).isEqualTo(orderEntity.quantity)
            assertThat(it.createdAt).isNotNull()
            assertThat(it.updatedAt).isNull()
        }
    }

    @Test
    fun `should map list of OrderEntity to list of Order`() {
        // Given
        val orderEntities = listOf(
            OrderEntity(
                id = 1L,
                orderNumber = "ORD-1",
                skuCode = "SKU-1",
                price = BigDecimal("100.00"),
                quantity = 1
            ),
            OrderEntity(
                id = 2L,
                orderNumber = "ORD-2",
                skuCode = "SKU-2",
                price = BigDecimal("200.00"),
                quantity = 2
            )
        )

        // When
        val result = orderEntities.toModels()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].orderNumber).isEqualTo("ORD-1")
        assertThat(result[1].id).isEqualTo(2L)
        assertThat(result[1].orderNumber).isEqualTo("ORD-2")
    }

    @Test
    fun `should map list of Order to list of OrderResponse`() {
        // Given
        val orders = listOf(
            Order(
                id = 1L,
                orderNumber = "ORD-1",
                skuCode = "SKU-1",
                price = BigDecimal("100.00"),
                quantity = 1,
                createdAt = LocalDateTime.now()
            ),
            Order(
                id = 2L,
                orderNumber = "ORD-2",
                skuCode = "SKU-2",
                price = BigDecimal("200.00"),
                quantity = 2,
                createdAt = LocalDateTime.now()
            )
        )

        // When
        val result = orders.toResponses()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].orderNumber).isEqualTo("ORD-1")
        assertThat(result[1].id).isEqualTo(2L)
        assertThat(result[1].orderNumber).isEqualTo("ORD-2")
    }

    @Suppress("UnusedPrivateMember")
    private fun orderRequestTestData() = listOf(
        Arguments.of(
            "IPHONE13-128",
            BigDecimal("999.99"),
            2
        ),
        Arguments.of(
            "SAMSUNG-S24",
            BigDecimal("1299.99"),
            1
        ),
        Arguments.of(
            "MACBOOK-PRO",
            BigDecimal("2499.99"),
            3
        ),
        Arguments.of(
            "AIRPODS-PRO",
            BigDecimal("249.99"),
            5
        )
    )

    @Suppress("UnusedPrivateMember")
    private fun orderToResponseTestData() = listOf(
        Arguments.of(
            1L,
            SAMPLE_ORDER_NUMBER,
            "IPHONE13-128",
            BigDecimal("999.99"),
            2
        ),
        Arguments.of(
            null,
            "ORD-TEMP-123",
            "SAMSUNG-S24",
            BigDecimal("1299.99"),
            1
        ),
        Arguments.of(
            99L,
            "ORD-SPECIAL-999",
            "MACBOOK-PRO",
            BigDecimal("2499.99"),
            3
        )
    )

    @Suppress("UnusedPrivateMember")
    private fun orderToEntityTestData() = listOf(
        Arguments.of(
            1L,
            SAMPLE_ORDER_NUMBER,
            "IPHONE13-128",
            BigDecimal("999.99"),
            2
        ),
        Arguments.of(
            null,
            "ORD-NEW-456",
            "SAMSUNG-S24",
            BigDecimal("1299.99"),
            1
        )
    )

    @Suppress("UnusedPrivateMember")
    private fun entityToModelTestData() = listOf(
        Arguments.of(
            1L,
            SAMPLE_ORDER_NUMBER,
            "IPHONE13-128",
            BigDecimal("999.99"),
            2
        ),
        Arguments.of(
            2L,
            "ORD-FROM-DB-789",
            "SAMSUNG-S24",
            BigDecimal("1299.99"),
            1
        )
    )
}