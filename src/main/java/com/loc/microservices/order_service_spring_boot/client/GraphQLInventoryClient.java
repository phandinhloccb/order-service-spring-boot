package com.loc.microservices.order_service_spring_boot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.Map;

@Component
@Primary
public class GraphQLInventoryClient implements InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(GraphQLInventoryClient.class);
    
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    public boolean isInStock(String skuCode, Integer quantity) {
        try {
            log.info("Checking inventory via GraphQL for skuCode: {}, quantity: {}", skuCode, quantity);
            
            // Chuẩn bị GraphQL query
            String graphqlQuery = String.format(
                "{\"query\":\"query { isInStock(skuCode: \\\"%s\\\", quantity: %d) }\"}",
                skuCode, quantity);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(graphqlQuery, headers);
            
            log.info("Sending GraphQL request to: {}", inventoryServiceUrl);
            
            // Gọi API GraphQL
            Map<String, Object> response = restTemplate.postForObject(
                inventoryServiceUrl, 
                request,    
                Map.class
            );
            
            log.info("Received GraphQL response: {}", response);
            
            if (response != null && response.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                if (data != null && data.containsKey("isInStock")) {
                    boolean isInStock = (Boolean) data.get("isInStock");
                    log.info("GraphQL inventory result for {}: {}", skuCode, isInStock);
                    return isInStock;
                }
            }
            
            // Kiểm tra lỗi trong response
            if (response != null && response.containsKey("errors")) {
                Object errors = response.get("errors");
                log.error("GraphQL errors: {}", errors);
                return false;
            }
            
            log.warn("GraphQL response format unexpected: {}", response);
            return false;
            
        } catch (Exception e) {
            log.error("Error calling inventory GraphQL API: {}", e.getMessage(), e);
            return fallbackMethod(skuCode, quantity, e);
        }
    }
    
    public boolean fallbackMethod(String skuCode, Integer quantity, Exception exception) {
        log.info("Cannot get inventory for skuCode: {}, quantity: {}, exception: {}", 
                skuCode, quantity, exception.getMessage());
        return false;
    }
} 