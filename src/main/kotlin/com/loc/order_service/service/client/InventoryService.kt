package com.loc.order_service.service.client

import com.loc.order_service.exception.domain.InventoryCheckException
import com.loc.order_service.exception.infrastructure.*
import com.loc.order_service.model.InventoryResult
import com.loc.orderservice.client.inventory.api.DefaultApi
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.net.ConnectException
import java.util.concurrent.TimeoutException

@Service
class InventoryService(
    private val inventoryApi: DefaultApi
) {
    private val log = KotlinLogging.logger {}
    
    suspend fun checkStock(skuCode: String, quantity: Int): InventoryResult {
        log.info("Checking inventory for SKU: $skuCode, quantity: $quantity")
        
        return try {
            val available = inventoryApi.checkStock(skuCode, quantity).awaitSingle()
            if (available) InventoryResult.InStock else InventoryResult.OutOfStock
        } catch (ex: Exception) {
            handleInventoryServiceException(ex, skuCode)
        }
    }
    
    private fun handleInventoryServiceException(ex: Exception, skuCode: String): Nothing {
        when (ex) {
            // WebClient Response Exceptions (4xx, 5xx)
            is WebClientResponseException -> {
                when {
                    ex.statusCode.is4xxClientError -> {
                        throw ExternalServiceClientException(
                            serviceName = "Inventory Service",
                            statusCode = ex.statusCode.value(),
                            message = "Invalid request for SKU: $skuCode - ${ex.responseBodyAsString}",
                            cause = ex
                        )
                    }
                    ex.statusCode.is5xxServerError -> {
                        throw ExternalServiceServerException(
                            serviceName = "Inventory Service",
                            statusCode = ex.statusCode.value(),
                            message = "Inventory service server error for SKU: $skuCode",
                            cause = ex
                        )
                    }
                    else -> {
                        throw InventoryCheckException(
                            message = "Unexpected response from inventory service for SKU: $skuCode",
                            cause = ex
                        )
                    }
                }
            }
            
            // WebClient Request Exceptions (network issues)
            is WebClientRequestException -> {
                when (ex.cause) {
                    is ConnectException -> {
                        throw ExternalServiceUnavailableException(
                            serviceName = "Inventory Service",
                            message = "Cannot connect to inventory service for SKU: $skuCode",
                            cause = ex
                        )
                    }
                    is TimeoutException -> {
                        throw ExternalServiceTimeoutException(
                            serviceName = "Inventory Service",
                            message = "Request timeout while checking inventory for SKU: $skuCode",
                            cause = ex
                        )
                    }
                    else -> {
                        throw NetworkException(
                            message = "Network error while checking inventory for SKU: $skuCode",
                            cause = ex
                        )
                    }
                }
            }
            
            // Timeout exceptions
            is TimeoutException -> {
                throw ExternalServiceTimeoutException(
                    serviceName = "Inventory Service",
                    message = "Timeout while checking inventory for SKU: $skuCode",
                    cause = ex
                )
            }
            
            // Generic exceptions
            else -> {
                throw InventoryCheckException(
                    message = "Failed to check inventory for SKU: $skuCode - ${ex.message}",
                    cause = ex
                )
            }
        }
    }
}

