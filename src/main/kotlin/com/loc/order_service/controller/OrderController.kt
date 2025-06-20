package com.loc.order_service.controller

import com.loc.order_service.mapper.toModel
import com.loc.order_service.mapper.toResponse
import com.loc.order_service.mapper.toResponses
import com.loc.order_service.model.Order
import com.loc.orderservice.model.OrderRequest
import com.loc.orderservice.model.OrderResponse
import com.loc.order_service.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        val createdOrder: Order = orderService.createOrder(orderRequest.toModel())
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder.toResponse())
    }
}
