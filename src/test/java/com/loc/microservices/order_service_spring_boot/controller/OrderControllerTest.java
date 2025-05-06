package com.loc.microservices.order_service_spring_boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loc.microservices.order_service_spring_boot.dto.OrderRequest;
import com.loc.microservices.order_service_spring_boot.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    void placeOrder_withValidRequest_shouldReturnSuccess() throws Exception {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(
            null, // id
            null, // orderNumber
            "test-product", // skuCode
            BigDecimal.valueOf(100.0), // price
            1 // quantity
        );
        
        doNothing().when(orderService).placeOrder(any(OrderRequest.class));

        // Act & Assert
        mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed successfully"));
    }

    @Test
    void placeOrder_whenProductNotInStock_shouldReturnError() throws Exception {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(
            null, // id
            null, // orderNumber
            "out-of-stock", // skuCode
            BigDecimal.valueOf(100.0), // price
            1 // quantity
        );
        
        doThrow(new RuntimeException("Product with Skucode: out-of-stock is not in stock"))
                .when(orderService).placeOrder(any(OrderRequest.class));

        // Act & Assert
        mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isInternalServerError());
    }
} 