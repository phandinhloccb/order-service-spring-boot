package com.loc.microservices.order_service_spring_boot.repository;

import com.loc.microservices.order_service_spring_boot.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void saveOrder_shouldPersistOrder() {
        // Arrange
        Order order = new Order();
        order.setOrderNumber("TEST-1234");
        order.setSkuCode("test-product");
        order.setPrice(BigDecimal.valueOf(100.0));
        order.setQuantity(2);

        // Act
        Order savedOrder = orderRepository.save(order);

        // Assert
        assertNotNull(savedOrder.getId());
        assertEquals("TEST-1234", savedOrder.getOrderNumber());
        assertEquals("test-product", savedOrder.getSkuCode());
        assertEquals(BigDecimal.valueOf(100.0), savedOrder.getPrice());
        assertEquals(2, savedOrder.getQuantity());
    }

    @Test
    void findById_shouldReturnOrder() {
        // Arrange
        Order order = new Order();
        order.setOrderNumber("FIND-1234");
        order.setSkuCode("find-product");
        order.setPrice(BigDecimal.valueOf(200.0));
        order.setQuantity(1);
        Order savedOrder = orderRepository.save(order);

        // Act
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        // Assert
        assertTrue(foundOrder.isPresent());
        assertEquals("FIND-1234", foundOrder.get().getOrderNumber());
    }
} 