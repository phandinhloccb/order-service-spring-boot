package com.loc.order_service.controller

import com.loc.order_service.mapper.toModel
import com.loc.order_service.mapper.toResponse
import com.loc.order_service.model.OrderResult
import com.loc.orderservice.model.OrderRequest
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
    suspend fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
        return when (val result = orderService.createOrder(orderRequest.toModel())) {
            is OrderResult.Success -> ResponseEntity.status(HttpStatus.CREATED).body(result.order.toResponse())
            is OrderResult.BusinessFailure -> ResponseEntity.unprocessableEntity().body(mapOf("error" to result.reason))
        }
    }
}
