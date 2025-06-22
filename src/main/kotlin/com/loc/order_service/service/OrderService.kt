package com.loc.order_service.service

import com.loc.order_service.mapper.*
import com.loc.order_service.model.InventoryResult
import com.loc.order_service.model.Order
import com.loc.order_service.model.OrderResult
import com.loc.order_service.repository.OrderRepository
import com.loc.order_service.service.client.InventoryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val inventoryService: InventoryService
) {
    @Transactional
    suspend fun createOrder(order: Order): OrderResult {
        val log = KotlinLogging.logger {}
        log.info { "Creating order for SKU: ${order.skuCode}" }

        return when (val stock = inventoryService.checkStock(order.skuCode, order.quantity)) {
            InventoryResult.InStock -> {
                val saved = withContext(Dispatchers.IO) {
                    orderRepository.save(order.toEntity())
                }
                OrderResult.Success(saved.toModel())
            }
            InventoryResult.OutOfStock -> {
                OrderResult.BusinessFailure("Product ${order.skuCode} is out of stock")
            }
        }
    }
}