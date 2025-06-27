package com.loc.order_service.domain.model
 
sealed class OrderResult {
    data class Success(val order: Order) : OrderResult()
    data class BusinessFailure(val reason: String) : OrderResult()
} 