// package com.loc.order_service.service.client

// import io.generated.inventory.client.InventoryApi
// import kotlinx.coroutines.runBlocking
// import mu.KotlinLogging
// import org.springframework.stereotype.Service

// @Service
// class InventoryService(
//     private val inventoryApi: InventoryApi
// ) {
//     private val log = KotlinLogging.logger {}
    
//     fun checkStock(skuCode: String, quantity: Int): Boolean = runBlocking {
//         runCatching {
//             log.info("Checking inventory for SKU: $skuCode, quantity: $quantity")
//             inventoryApi.checkStock(skuCode, quantity)
//         }.onFailure { exception ->
//             log.error(exception) { "Failed to check inventory for SKU: $skuCode" }
//         }.getOrElse { false }
//     }
// }