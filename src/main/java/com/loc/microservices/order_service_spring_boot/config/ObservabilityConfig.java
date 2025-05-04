package com.loc.microservices.order_service_spring_boot.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import com.loc.microservices.order_service_spring_boot.event.OrderPlacedEvent;

@Configuration
public class ObservabilityConfig {

    @Autowired(required = false)
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @PostConstruct
    public void setObservationForKafkaTemplate() {
        if (kafkaTemplate != null) {
            kafkaTemplate.setObservationEnabled(true);
        }
    }

    @Bean
    ObservedAspect observedAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }
}

