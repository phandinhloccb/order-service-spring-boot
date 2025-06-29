/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
*/
package com.loc.orderservice.api

import com.loc.orderservice.model.ErrorResponse
import com.loc.orderservice.model.OrderRequest
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid

import kotlin.collections.List
import kotlin.collections.Map

@Validated
@RequestMapping("\${api.base-path:}")
interface DefaultApi {

    fun getDelegate(): DefaultApiDelegate = object: DefaultApiDelegate {}

    @Operation(
        tags = ["default",],
        summary = "Place a new order",
        operationId = "placeOrder",
        description = """Create a new order in the system""",
        responses = [
            ApiResponse(responseCode = "200", description = "Order placed successfully", content = [Content(schema = Schema(implementation = kotlin.String::class))]),
            ApiResponse(responseCode = "400", description = "Invalid request", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
            ApiResponse(responseCode = "404", description = "Products not found in inventory", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
        ]
    )
    @RequestMapping(
            method = [RequestMethod.POST],
            value = ["/api/order"],
            produces = ["text/plain", "application/json"],
            consumes = ["application/json"]
    )
    fun placeOrder(@Parameter(description = "Order information to be placed", required = true) @Valid @RequestBody orderRequest: OrderRequest): ResponseEntity<kotlin.String> {
        return getDelegate().placeOrder(orderRequest)
    }
}
