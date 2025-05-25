package com.loc.microservices.order_service_spring_boot.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.loc.microservices.order_service_spring_boot.client.InventoryClient;
import com.loc.microservices.order_service_spring_boot.dto.OrderRequest;
import com.loc.microservices.order_service_spring_boot.event.OrderPlacedEvent;
import com.loc.microservices.order_service_spring_boot.exception.ProductNotFoundException;
import com.loc.microservices.order_service_spring_boot.model.Order;
import com.loc.microservices.order_service_spring_boot.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private InventoryClient inventoryClient;
    
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Using inventory client implementation: {}", inventoryClient.getClass().getName());
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        
        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);

            // send kafka message
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            
            log.info("Order placed event start sent to kafka topic: {}", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("Order placed event end sent to kafka topic: {}", orderPlacedEvent);

        } else {
            log.warn("Product not found or out of stock: {}", orderRequest.skuCode());
            throw new ProductNotFoundException(orderRequest.skuCode());
        }
    }
}