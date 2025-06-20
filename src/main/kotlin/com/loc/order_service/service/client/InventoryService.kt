package com.loc.order_service.service.client

import com.loc.order_service.model.InventoryResult
import com.loc.orderservice.client.inventory.api.DefaultApi
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class InventoryService(
    private val inventoryApi: DefaultApi
) {
    private val log = KotlinLogging.logger {}
    
    fun checkStock(skuCode: String, quantity: Int): InventoryResult {
        log.info("Checking inventory for SKU: $skuCode, quantity: $quantity")
        val available = inventoryApi.checkStock(skuCode, quantity).block()!!
        return if (available) InventoryResult.InStock else InventoryResult.OutOfStock
    }
}

