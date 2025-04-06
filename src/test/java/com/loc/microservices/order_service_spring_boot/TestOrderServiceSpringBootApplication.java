package com.loc.microservices.order_service_spring_boot;

import org.springframework.boot.SpringApplication;

public class TestOrderServiceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderServiceSpringBootApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
