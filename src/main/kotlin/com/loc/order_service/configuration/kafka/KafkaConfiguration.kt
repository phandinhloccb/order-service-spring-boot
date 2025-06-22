package com.loc.order_service.configuration.kafka

import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(KafkaOutputTopics::class)
@Configuration
class KafkaConfiguration

@ConfigurationProperties(prefix = "spring.kafka.output.topic")
@ConstructorBinding
class KafkaOutputTopics(
    val orderCompleted: String
)
