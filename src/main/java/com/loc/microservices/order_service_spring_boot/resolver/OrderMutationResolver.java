package com.loc.microservices.order_service_spring_boot.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.loc.microservices.order_service_spring_boot.dto.OrderInput;
import com.loc.microservices.order_service_spring_boot.dto.OrderRequest;
import com.loc.microservices.order_service_spring_boot.service.OrderService;

@Controller
public class OrderMutationResolver {
    
    private static final Logger log = LoggerFactory.getLogger(OrderMutationResolver.class);
    
    @Autowired
    private OrderService orderService;
    
    @MutationMapping
    public String placeOrder(@Argument OrderInput orderInput) {
        // Convert from OrderInput to OrderRequest
        OrderRequest orderRequest = new OrderRequest(
            null,
            null,
            orderInput.getSkuCode(),
            orderInput.getPrice(),
            orderInput.getQuantity()
        );
        
        try {
            orderService.placeOrder(orderRequest);
            return "Order placed successfully";
        } catch (Exception e) {
            log.error("Error placing order: {}", e.getMessage(), e);
            throw e; // GraphQLExceptionHandler sẽ xử lý
        }
    }
}