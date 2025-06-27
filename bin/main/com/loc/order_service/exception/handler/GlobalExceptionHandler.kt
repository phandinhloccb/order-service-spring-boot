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

    @ExceptionHandler(ExternalServiceUnavailableException::class)
    fun handleExternalServiceUnavailable(ex: ExternalServiceUnavailableException): ResponseEntity<ErrorResponse> {
        log.error("External service unavailable: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.SERVICE_UNAVAILABLE.value(),
                    error = "External Service Unavailable",
                    message = ex.message ?: "External service is currently unavailable",
                    path = "/api/orders"
                )
            )
    }

    @ExceptionHandler(ExternalServiceTimeoutException::class)
    fun handleExternalServiceTimeout(ex: ExternalServiceTimeoutException): ResponseEntity<ErrorResponse> {
        log.error("External service timeout: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.REQUEST_TIMEOUT.value(),
                    error = "Service Timeout",
                    message = ex.message ?: "External service request timed out",
                    path = "/api/orders"
                )
            )
    }

    @ExceptionHandler(ExternalServiceClientException::class)
    fun handleExternalServiceClientError(ex: ExternalServiceClientException): ResponseEntity<ErrorResponse> {
        log.error("External service client error: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.BAD_REQUEST.value(),
                    error = "Client Error",
                    message = ex.message ?: "Invalid request to external service",
                    path = "/api/orders"
                )
            )
    }

    @ExceptionHandler(ExternalServiceServerException::class)
    fun handleExternalServiceServerError(ex: ExternalServiceServerException): ResponseEntity<ErrorResponse> {
        log.error("External service server error: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.BAD_GATEWAY.value(),
                    error = "External Service Error",
                    message = ex.message ?: "External service encountered an error",
                    path = "/api/orders"
                )
            )
    }

    @ExceptionHandler(NetworkException::class)
    fun handleNetworkException(ex: NetworkException): ResponseEntity<ErrorResponse> {
        log.error("Network error: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                ErrorResponse(
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.SERVICE_UNAVAILABLE.value(),
                    error = "Network Error",
                    message = ex.message ?: "Network connectivity issue",
                    path = "/api/orders"
                )
            )
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