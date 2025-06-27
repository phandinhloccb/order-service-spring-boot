package com.loc.order_service.exception.handler

import com.loc.order_service.exception.domain.*
import com.loc.order_service.exception.infrastructure.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    
    private val log = KotlinLogging.logger {}

    @ExceptionHandler(InventoryCheckException::class)
    fun handleInventoryCheckException(ex: InventoryCheckException): ResponseEntity<ErrorResponse> {
        log.error("Inventory check failed: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.SERVICE_UNAVAILABLE.value(),
                    error = "Inventory Service Unavailable",
                    message = ex.message ?: "Unable to check inventory at this time",
                    path = "/api/orders"
                )
            )
    }

    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(ex: InfrastructureException): ResponseEntity<ErrorResponse> {
        log.error("Infrastructure error: ${ex.message}", ex)
        val status = when (ex) {
            is ExternalServiceTimeoutException -> HttpStatus.REQUEST_TIMEOUT
            is ExternalServiceClientException -> HttpStatus.BAD_REQUEST
            is ExternalServiceServerException -> HttpStatus.BAD_GATEWAY
            is KafkaPublishException -> HttpStatus.SERVICE_UNAVAILABLE
            is DatabaseConnectionException -> HttpStatus.SERVICE_UNAVAILABLE
            else -> HttpStatus.SERVICE_UNAVAILABLE
        }
        return ResponseEntity.status(status)
            .body(ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = status.value(),
                error = "Infrastructure Error",
                message = ex.message ?: "An infrastructure error occurred",
                path = "/api/orders"
            ))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    error = "Internal Server Error",
                    message = "An unexpected error occurred",
                    path = "/api/orders"
                )
            )
    }

    data class ErrorResponse(
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
    )
} 