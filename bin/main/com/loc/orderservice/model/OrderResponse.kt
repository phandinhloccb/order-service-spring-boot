package com.loc.orderservice.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param id 
 * @param orderNumber 
 * @param skuCode 
 * @param price 
 * @param quantity 
 * @param status 
 * @param createdAt 
 * @param updatedAt 
 */
data class OrderResponse(

    @Schema(example = "1", description = "")
    @get:JsonProperty("id") var id: kotlin.Long? = null,

    @Schema(example = "f9b9a8b7-c6d5-4e3f-2g1h-0i9j8k7l6m5n", description = "")
    @get:JsonProperty("orderNumber") var orderNumber: kotlin.String? = null,

    @Schema(example = "IPHONE13-128", description = "")
    @get:JsonProperty("skuCode") var skuCode: kotlin.String? = null,

    @Schema(example = "999.99", description = "")
    @get:JsonProperty("price") var price: java.math.BigDecimal? = null,

    @Schema(example = "1", description = "")
    @get:JsonProperty("quantity") var quantity: kotlin.Int? = null,

    @Schema(example = "PENDING", description = "")
    @get:JsonProperty("status") var status: OrderResponse.Status? = null,

    @Schema(example = "2025-01-15T10:30Z", description = "")
    @get:JsonProperty("createdAt") var createdAt: java.time.OffsetDateTime? = null,

    @Schema(example = "2025-01-15T10:30Z", description = "")
    @get:JsonProperty("updatedAt") var updatedAt: java.time.OffsetDateTime? = null
) {

    /**
    * 
    * Values: PENDING,CONFIRMED,PROCESSING,SHIPPED,DELIVERED,CANCELLED,FAILED
    */
    enum class Status(val value: kotlin.String) {

        @JsonProperty("PENDING") PENDING("PENDING"),
        @JsonProperty("CONFIRMED") CONFIRMED("CONFIRMED"),
        @JsonProperty("PROCESSING") PROCESSING("PROCESSING"),
        @JsonProperty("SHIPPED") SHIPPED("SHIPPED"),
        @JsonProperty("DELIVERED") DELIVERED("DELIVERED"),
        @JsonProperty("CANCELLED") CANCELLED("CANCELLED"),
        @JsonProperty("FAILED") FAILED("FAILED")
    }

}

