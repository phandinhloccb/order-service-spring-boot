package com.loc.microservices.order_service_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceSpringBootApplication.class, args);
	}

}
