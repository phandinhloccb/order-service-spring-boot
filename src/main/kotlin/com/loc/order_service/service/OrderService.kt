package com.loc.order_service.service

import com.loc.order_service.entity.OrderEntity
import com.loc.order_service.mapper.*
import com.loc.order_service.model.InventoryResult
import com.loc.order_service.model.Order
import com.loc.order_service.model.OrderResult
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
    fun createOrder(order: Order): OrderResult {
        return when (val stock = inventoryService.checkStock(order.skuCode, order.quantity)) {
            InventoryResult.InStock -> {
                val entity = orderRepository.save(order.toEntity())
                OrderResult.Success(entity.toModel())
            }
            InventoryResult.OutOfStock -> {
                OrderResult.BusinessFailure("Product ${order.skuCode} is out of stock")
            }
        }
    }
}