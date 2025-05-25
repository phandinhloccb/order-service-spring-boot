package com.loc.microservices.order_service_spring_boot.resolver;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.loc.microservices.order_service_spring_boot.model.Order;

/**
 * Resolver cho các trường phức tạp hoặc calculated fields của Order type
 */
@Controller
public class OrderFieldResolver {
    
    /**
     * Ví dụ về cách định nghĩa một calculated field
     * Đây là một ví dụ, bạn có thể thêm các field khác tùy theo nhu cầu
     */
    @SchemaMapping(typeName = "Order", field = "totalPrice")
    public Double getTotalPrice(Order order) {
        if (order.getPrice() != null && order.getQuantity() != null) {
            return order.getPrice().doubleValue() * order.getQuantity();
        }
        return 0.0;
    }
} 