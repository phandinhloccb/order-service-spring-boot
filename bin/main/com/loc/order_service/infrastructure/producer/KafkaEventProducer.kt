package com.loc.order_service.infrastructure.producer

import com.loc.order_service.infrastructure.configuration.kafka.KafkaOutputTopics
import com.loc.order_service.exception.infrastructure.KafkaPublishException
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
        try {
            kafkaTemplate.send(
                ProducerRecord(
                    kafkaOutputTopics.orderCompleted,
                    orderCompletedEvent.orderId.toString(),
                    orderCompletedEvent
                )
            )
        } catch (ex: Exception) {
            throw KafkaPublishException(
                message = "Failed to publish OrderCompletedEvent for orderId=${orderCompletedEvent.orderId}",
                cause = ex
            )
        }

    }
}