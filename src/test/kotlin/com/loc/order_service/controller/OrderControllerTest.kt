package com.loc.order_service.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.loc.order_service.application.service.OrderService
import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.Order
import com.loc.order_service.domain.model.OrderResult
import com.loc.orderservice.model.OrderRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(OrderController::class)
@ExtendWith(MockKExtension::class)
internal class OrderControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Test
    fun `should return CREATED when creating a new order`() = runTest {
        // Given
        val orderRequest = OrderRequest(
            skuCode = "IPHONE13-128",
            price = BigDecimal("999.99"),
            quantity = 2
        )

        val createdOrder = Order(
            id = 1L,
            orderNumber = "ORD-1234567890-ABCD1234",
            skuCode = "IPHONE13-128",
            price = BigDecimal("999.99"),
            quantity = 2,
            status = OrderStatusEnum.CONFIRMED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { orderService.createOrder(any()) } returns OrderResult.Success(createdOrder)

        // When / Then
        Given {
            contentType(ContentType.JSON)
            body(objectMapper.writeValueAsString(orderRequest))
        } When {
            post("/api/orders")
        } Then {
            status(HttpStatus.CREATED)
            contentType(ContentType.JSON) // ✅ Verify content type
            body("id", equalTo(1))
            body("orderNumber", equalTo("ORD-1234567890-ABCD1234"))
            body("skuCode", equalTo("IPHONE13-128"))
            body("price", equalTo(999.99f))
            body("quantity", equalTo(2))
            body("status", equalTo("CONFIRMED"))
        }
    }

    @Test
    fun `should return UNPROCESSABLE_ENTITY when business failure occurs`() = runTest {
        // Given
        val orderRequest = OrderRequest(
            skuCode = "OUT-OF-STOCK-ITEM",
            price = BigDecimal("999.99"),
            quantity = 2
        )

        coEvery { orderService.createOrder(any()) } returns 
            OrderResult.BusinessFailure("Product OUT-OF-STOCK-ITEM is out of stock")

        // When / Then
        Given {
            contentType(ContentType.JSON)
            body(objectMapper.writeValueAsString(orderRequest))
        } When {
            post("/api/orders")
        } Then {
            status(HttpStatus.UNPROCESSABLE_ENTITY)
            contentType(ContentType.JSON) // ✅ Verify content type
            body("error", equalTo("Product OUT-OF-STOCK-ITEM is out of stock"))
        }
    }

    @Test
    fun `should handle large quantity orders`() = runTest {
        // Given
        val orderRequest = OrderRequest(
            skuCode = "BULK-ITEM-001",
            price = BigDecimal("50.00"),
            quantity = 100
        )

        val createdOrder = Order(
            id = 3L,
            orderNumber = "ORD-BULK-12345",
            skuCode = "BULK-ITEM-001",
            price = BigDecimal("50.00"),
            quantity = 100,
            status = OrderStatusEnum.CONFIRMED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { orderService.createOrder(any()) } returns OrderResult.Success(createdOrder)

        // When / Then
        Given {
            contentType(ContentType.JSON)
            body(objectMapper.writeValueAsString(orderRequest))
        } When {
            post("/api/orders")
        } Then {
            status(HttpStatus.CREATED)
            contentType(ContentType.JSON)
            body("quantity", equalTo(100))
            body("skuCode", equalTo("BULK-ITEM-001"))
        }
    }
}