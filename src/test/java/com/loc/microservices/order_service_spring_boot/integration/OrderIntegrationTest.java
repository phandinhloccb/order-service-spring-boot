package com.loc.microservices.order_service_spring_boot.integration;

import com.loc.microservices.order_service_spring_boot.client.InventoryClient;
import com.loc.microservices.order_service_spring_boot.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private InventoryClient inventoryClient;
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void createOrder_shouldReturnSuccessMessage() {
        // Arrange
        when(inventoryClient.isInStock(anyString(), anyInt())).thenReturn(true);
        when(kafkaTemplate.send(anyString(), any())).thenReturn(null);
        
        OrderRequest request = new OrderRequest(
            null, // id
            null, // orderNumber
            "test-sku", // skuCode
            BigDecimal.valueOf(100), // price
            1 // quantity
        );
        
        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/order", request, String.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed successfully", response.getBody());
    }
    
    @Test
    void createOrder_whenProductOutOfStock_shouldReturnError() {
        // Arrange
        when(inventoryClient.isInStock(anyString(), anyInt())).thenReturn(false);
        
        OrderRequest request = new OrderRequest(
            null, // id
            null, // orderNumber
            "out-of-stock", // skuCode
            BigDecimal.valueOf(100), // price
            1 // quantity
        );
        
        // Act
        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/api/order", request, Object.class);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
} 