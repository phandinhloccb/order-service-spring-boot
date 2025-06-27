package com.loc.order_service.exception.domain

abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class InventoryCheckException(
    message: String,
    cause: Throwable? = null
) : DomainException(message, cause) 