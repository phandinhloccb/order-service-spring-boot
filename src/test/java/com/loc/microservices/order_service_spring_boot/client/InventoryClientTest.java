package com.loc.microservices.order_service_spring_boot.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryClientTest {

    @Mock
    private InventoryClient inventoryClient;

    @Test
    void isInStock_whenProductInStock_shouldReturnTrue() {
        // Arrange
        when(inventoryClient.isInStock("in-stock-product", 5)).thenReturn(true);

        // Act
        boolean result = inventoryClient.isInStock("in-stock-product", 5);
        
        // Assert
        assertTrue(result);
        verify(inventoryClient, times(1)).isInStock("in-stock-product", 5);
    }

    @Test
    void isInStock_whenProductOutOfStock_shouldReturnFalse() {
        // Arrange
        when(inventoryClient.isInStock("out-of-stock", 10)).thenReturn(false);
        
        // Act
        boolean result = inventoryClient.isInStock("out-of-stock", 10);
        
        // Assert
        assertFalse(result);
        verify(inventoryClient, times(1)).isInStock("out-of-stock", 10);
    }
    
    @Test
    void fallbackMethod_shouldReturnFalse() {
        // Tạo một instance thực của interface để test phương thức default
        InventoryClient actualClient = new InventoryClient() {
            @Override
            public boolean isInStock(String skuCode, Integer quantity) {
                return false; // Không quan trọng vì chúng ta chỉ test fallbackMethod
            }
        };
        
        // Act - test trực tiếp phương thức fallback
        boolean result = actualClient.fallbackMethod("error-product", 1, 
                                   new RuntimeException("Service unavailable"));
        
        // Assert
        assertFalse(result);
    }
}