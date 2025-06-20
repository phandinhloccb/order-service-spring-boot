package com.loc.order_service.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.loc.order_service.model.Order
import com.loc.order_service.service.OrderService
import com.loc.orderservice.model.OrderRequest
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(OrderController::class)
@ExtendWith(MockKExtension::class)
internal class OrderControllerTest (
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @MockK
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Test
    fun `should return CREATED when creating a new order`() {
        // Given
        val orderRequest = OrderRequest(
            skuCode = "123",
            price = BigDecimal("999.99"),
            quantity = 2
        )

        val createdOrder = Order(
            id = 1L,
            orderNumber = "ORD-1234567890-ABCD1234",
            skuCode = "IPHONE13-128",
            price = BigDecimal("999.99"),
            quantity = 2,
            createdAt = LocalDateTime.now()
        )

        every { orderService.createOrder(any()) } returns createdOrder

        //When / Then
        Given {
            contentType(ContentType.JSON)
            body(objectMapper.writeValueAsString(orderRequest))
        } When  {
            post("/api/orders")
        } Then  {
            status(HttpStatus.CREATED)
        }
    }
}