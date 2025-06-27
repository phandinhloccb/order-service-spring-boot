package com.loc.order_service.infrastructure.configuration.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(KafkaOutputTopics::class)
@Configuration
class KafkaConfiguration

@ConfigurationProperties(prefix = "spring.kafka.output.topic")
class KafkaOutputTopics(
    val orderCompleted: String
)
