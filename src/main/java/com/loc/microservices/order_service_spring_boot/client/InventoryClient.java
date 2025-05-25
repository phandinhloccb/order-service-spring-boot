package com.loc.microservices.order_service_spring_boot.client;

/**
 * Interface for inventory client that can be implemented by different types of clients
 * (REST, GraphQL, etc.)
 */
public interface InventoryClient {
    
    /**
     * Check if a product is in stock
     * 
     * @param skuCode the product code
     * @param quantity the quantity requested
     * @return true if product is in stock with requested quantity, false otherwise
     */
    boolean isInStock(String skuCode, Integer quantity);
}
