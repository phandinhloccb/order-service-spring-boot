package com.loc.order_service.service.client

import com.loc.orderservice.client.inventory.api.DefaultApi
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class InventoryService(
    private val inventoryApi: DefaultApi
) {
    private val log = KotlinLogging.logger {}
    
    fun checkStock(skuCode: String, quantity: Int): Boolean {
        return runCatching {
            log.info("Checking inventory for SKU: $skuCode, quantity: $quantity")
            inventoryApi.checkStock(skuCode, quantity).block() ?: false
        }.onFailure { exception ->
            log.error(exception) { "Failed to check inventory for SKU: $skuCode" }
        }.getOrElse { false }
    }
}
