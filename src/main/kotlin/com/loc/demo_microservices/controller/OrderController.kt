package com.loc.demo_microservices.controller

import com.loc.demo_microservices.mapper.toModelWithValidation
import com.loc.demo_microservices.mapper.toResponse
import com.loc.demo_microservices.mapper.toResponses
import com.loc.demo_microservices.model.Order
import com.loc.orderservice.model.OrderRequest
import com.loc.orderservice.model.OrderResponse
import com.loc.demo_microservices.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@RequestBody request: OrderRequest): ResponseEntity<OrderResponse> {
        val result = request.toModelWithValidation()
        
        return result.fold(
            onSuccess = {
                val createdOrder: Order = orderService.createOrder(it)
                ResponseEntity.ok(createdOrder.toResponse())
            },
            onFailure = {
                ResponseEntity.badRequest().body(null)
            }
        )
    }
}
