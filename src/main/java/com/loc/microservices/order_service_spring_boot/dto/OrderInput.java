package com.loc.microservices.order_service_spring_boot.dto;

import java.math.BigDecimal;

/**
 * GraphQL input type for placing orders
 */
public class OrderInput {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    
    public OrderInput() {
    }
    
    public OrderInput(String skuCode, BigDecimal price, Integer quantity) {
        this.skuCode = skuCode;
        this.price = price;
        this.quantity = quantity;
    }
    
    public String getSkuCode() {
        return skuCode;
    }
    
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
} 