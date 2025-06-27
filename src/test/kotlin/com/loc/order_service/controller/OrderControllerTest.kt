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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.math.BigDecimal
import java.time.LocalDateTime

@WebFluxTest(OrderController::class)
@ExtendWith(MockKExtension::class)
internal class OrderControllerTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var orderService: OrderService

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
        webTestClient.post()
            .uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.orderNumber").isEqualTo("ORD-1234567890-ABCD1234")
            .jsonPath("$.skuCode").isEqualTo("IPHONE13-128")
            .jsonPath("$.quantity").isEqualTo(2)
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
        webTestClient.post()
            .uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.error").isEqualTo("Product OUT-OF-STOCK-ITEM is out of stock")
    }
}
