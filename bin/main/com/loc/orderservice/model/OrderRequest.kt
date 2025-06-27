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
 * @param skuCode 
 * @param price 
 * @param quantity 
 */
data class OrderRequest(

    @Schema(example = "IPHONE13-128", required = true, description = "")
    @get:JsonProperty("skuCode", required = true) var skuCode: kotlin.String,

    @Schema(example = "999.99", required = true, description = "")
    @get:JsonProperty("price", required = true) var price: java.math.BigDecimal,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("quantity", required = true) var quantity: kotlin.Int
) {

}

