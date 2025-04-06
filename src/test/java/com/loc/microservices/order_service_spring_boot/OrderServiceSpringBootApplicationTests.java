package com.loc.microservices.order_service_spring_boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderServiceSpringBootApplicationTests {

	@Test
	void contextLoads() {
	}

}
