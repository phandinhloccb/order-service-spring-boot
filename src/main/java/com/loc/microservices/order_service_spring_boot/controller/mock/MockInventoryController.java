package com.loc.microservices.order_service_spring_boot.controller.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mock/api/inventory")
@Profile("dev-mock")
public class MockInventoryController {
    
    private static final Logger log = LoggerFactory.getLogger(MockInventoryController.class);

    @GetMapping
    public boolean isInStock(
            @RequestParam String skuCode,
            @RequestParam Integer quantity) {
        
        log.info("Mock inventory check: skuCode={}, quantity={}", skuCode, quantity);
        
        boolean isInStock = !skuCode.startsWith("OUT");
        
        if (skuCode.startsWith("LOW")) {
            isInStock = quantity <= 5;
        }
        
        log.info("Mock inventory result for {}: {}", skuCode, isInStock);
        return isInStock;
    }
    
    // 404 error response
    @GetMapping("/error-demo")
    public ResponseEntity<Map<String, Object>> errorDemo() {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", java.time.OffsetDateTime.now().toString());
        errorResponse.put("status", 404);
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", "Product with Skucode: DEMO-ERROR not found in inventory");
        errorResponse.put("path", "/api/inventory");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}