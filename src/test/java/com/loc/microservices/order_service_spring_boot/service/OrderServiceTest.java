package com.loc.microservices.order_service_spring_boot.service;

import com.loc.microservices.order_service_spring_boot.client.InventoryClient;
import com.loc.microservices.order_service_spring_boot.dto.OrderRequest;
import com.loc.microservices.order_service_spring_boot.event.OrderPlacedEvent;
import com.loc.microservices.order_service_spring_boot.model.Order;
import com.loc.microservices.order_service_spring_boot.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        // Sử dụng reflection để set các mock objects
        ReflectionTestUtils.setField(orderService, "orderRepository", orderRepository);
        ReflectionTestUtils.setField(orderService, "inventoryClient", inventoryClient);
        ReflectionTestUtils.setField(orderService, "kafkaTemplate", kafkaTemplate);
    }

    @Test
    void placeOrder_whenProductInStock_shouldSaveOrderAndSendKafkaMessage() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(
            null, 
            null, 
            "iphone_13", 
            BigDecimal.valueOf(999.99), 
            1 
        );
        
        when(inventoryClient.isInStock("iphone_13", 1)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        orderService.placeOrder(orderRequest);

        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaTemplate, times(1)).send(eq("order-placed"), any(OrderPlacedEvent.class));

        // Capture the saved order
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertEquals("iphone_13", savedOrder.getSkuCode());
        assertEquals(BigDecimal.valueOf(999.99), savedOrder.getPrice());
        assertEquals(1, savedOrder.getQuantity());
        assertNotNull(savedOrder.getOrderNumber());
    }

    @Test
    void placeOrder_whenProductOutOfStock_shouldThrowException() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest(
            null, // id
            null, // orderNumber
            "out_of_stock_product", // skuCode
            BigDecimal.valueOf(999.99), // price
            1 // quantity
        );
        
        when(inventoryClient.isInStock("out_of_stock_product", 1)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        assertEquals("Product with Skucode: out_of_stock_product is not in stock", exception.getMessage());
        
        // Verify no interactions with repository or kafka
        verify(orderRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }
} 