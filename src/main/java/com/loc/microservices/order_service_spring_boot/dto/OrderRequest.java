package com.loc.microservices.order_service_spring_boot.dto;

import java.math.BigDecimal;

public record OrderRequest(
    Long id,
    String orderNumber,
    String skuCode,
    BigDecimal price,
    Integer quantity
) {
    
}
