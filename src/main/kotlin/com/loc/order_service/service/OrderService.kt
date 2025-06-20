package com.loc.order_service.service

import com.loc.order_service.entity.OrderEntity
import com.loc.order_service.mapper.*
import com.loc.order_service.model.Order
import com.loc.order_service.repository.OrderRepository
import com.loc.order_service.service.client.InventoryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val inventoryService: InventoryService
) {
    @Transactional
    fun createOrder(order: Order): Order {
        val isInStock = inventoryService.checkStock(
            skuCode = order.skuCode,
            quantity = order.quantity
        )
        
        if (!isInStock) {
            throw RuntimeException("Product ${order.skuCode} is out of stock")
        }
        
        val saveEntity: OrderEntity = orderRepository.save(order.toEntity())
        return saveEntity.toModel()
    }
}