package com.loc.order_service.model
 
sealed class InventoryResult {
    object InStock : InventoryResult()
    object OutOfStock : InventoryResult()
} 