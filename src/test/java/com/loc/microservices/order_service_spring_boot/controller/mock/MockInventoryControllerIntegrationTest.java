package com.loc.microservices.order_service_spring_boot.controller.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev-mock")
public class MockInventoryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void isInStock_withNormalProduct_shouldReturnTrue() {
        // Act
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/mock/api/inventory?skuCode=normal-product&quantity=1", Boolean.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void isInStock_withOutProduct_shouldReturnFalse() {
        // Act
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/mock/api/inventory?skuCode=OUT-product&quantity=1", Boolean.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void isInStock_withLowProductAndHighQuantity_shouldReturnFalse() {
        // Act
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/mock/api/inventory?skuCode=LOW-product&quantity=10", Boolean.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void isInStock_withLowProductAndLowQuantity_shouldReturnTrue() {
        // Act
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/mock/api/inventory?skuCode=LOW-product&quantity=3", Boolean.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
} 