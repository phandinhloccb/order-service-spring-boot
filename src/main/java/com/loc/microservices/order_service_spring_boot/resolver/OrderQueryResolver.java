package com.loc.microservices.order_service_spring_boot.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loc.microservices.order_service_spring_boot.model.Order;
import com.loc.microservices.order_service_spring_boot.repository.OrderRepository;

import java.util.List;

@Controller
public class OrderQueryResolver {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @QueryMapping
    public Order orderById(@Argument Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Order> allOrders() {
        return orderRepository.findAll();
    }
    
    @QueryMapping
    public Order orderByOrderNumber(@Argument String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }
} 