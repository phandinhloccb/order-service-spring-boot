package com.loc.order_service.domain.model
 
sealed class InventoryResult {
    object InStock : InventoryResult()
    object OutOfStock : InventoryResult()
} 