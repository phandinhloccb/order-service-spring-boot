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
    val order = orderService.getOrder(id)
    return if (order != null) {
        ResponseEntity.ok(order.toResponse())
    } else {
        ResponseEntity.notFound().build()
    }
}
