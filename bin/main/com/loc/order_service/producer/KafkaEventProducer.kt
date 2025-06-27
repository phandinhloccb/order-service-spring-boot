package com.loc.order_service.producer

import com.loc.order_service.configuration.kafka.KafkaOutputTopics
import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val kafkaOutputTopics: KafkaOutputTopics
) {
    fun publishOrderCompletedEvent(
        orderCompletedEvent: OrderCompletedEvent
    ) {
        kafkaTemplate.send(
            ProducerRecord(
                kafkaOutputTopics.orderCompleted,
                orderCompletedEvent.orderId.toString(),
                orderCompletedEvent
            )
        )
    }
}