package com.loc.order_service.controller

import com.loc.order_service.controller.mapper.toModel
import com.loc.order_service.controller.mapper.toResponse
import com.loc.order_service.domain.model.OrderResult
import com.loc.orderservice.model.OrderRequest
import com.loc.order_service.application.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
/**
 * OrderController handles order-related operations.
 * - POST /api/orders: Create a new order
 * - GET /api/orders/{id}: Retrieve an existing order
 * Note: POST /api/orders/{id} is not supported. Use POST /api/orders to create new orders.
 */
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    suspend fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
        return when (val result = orderService.createOrder(orderRequest.toModel())) {
            is OrderResult.Success -> ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.order.toResponse())
                
            is OrderResult.BusinessFailure -> ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapOf("error" to result.reason))
        }
    }
}

@GetMapping("/{id}")
suspend fun getOrder(@PathVariable id: Long): ResponseEntity<Any> {
    // INTENTIONALLY BUGGY for agent test:
    // getOrder may return null -> NPE at toResponse()
    val order = orderService.getOrder(id)
    return ResponseEntity.ok(order!!.toResponse())
}
