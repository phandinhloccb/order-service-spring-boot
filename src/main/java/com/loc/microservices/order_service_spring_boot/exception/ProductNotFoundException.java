package com.loc.microservices.order_service_spring_boot.exception;

public class ProductNotFoundException extends RuntimeException {
    private final String skuCode;

    public ProductNotFoundException(String skuCode) {
        super("Product with skuCode: " + skuCode + " is not in stock or does not exist");
        this.skuCode = skuCode;
    }

    public String getSkuCode() {
        return skuCode;
    }
}