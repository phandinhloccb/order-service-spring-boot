package com.loc.orderservice.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
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
 * @param timestamp 
 * @param status 
 * @param error 
 * @param message 
 * @param path 
 */
data class ErrorResponse(

    @Schema(example = "2023-05-20T14:30:15.123Z", description = "")
    @get:JsonProperty("timestamp") var timestamp: java.time.OffsetDateTime? = null,

    @Schema(example = "404", description = "")
    @get:JsonProperty("status") var status: kotlin.Int? = null,

    @Schema(example = "Not Found", description = "")
    @get:JsonProperty("error") var error: kotlin.String? = null,

    @Schema(example = "Product with Skucode: IPHONE13-128 is not in stock", description = "")
    @get:JsonProperty("message") var message: kotlin.String? = null,

    @Schema(example = "/api/order", description = "")
    @get:JsonProperty("path") var path: kotlin.String? = null
) {

}

