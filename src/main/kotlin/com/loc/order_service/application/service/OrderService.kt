package com.loc.order_service.application.service

import com.loc.order_service.application.port.repository.OrderDomainRepository
import com.loc.order_service.application.service.client.InventoryService
import com.loc.order_service.domain.enum.OrderStatusEnum
import com.loc.order_service.domain.model.InventoryResult
import com.loc.order_service.domain.model.Order
import com.loc.order_service.domain.model.OrderResult
import com.loc.order_service.infrastructure.mapper.toEntity
import com.loc.order_service.infrastructure.mapper.toModel
import com.loc.order_service.infrastructure.producer.KafkaEventProducer
import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderDomainRepository: OrderDomainRepository,
    private val inventoryService: InventoryService,
    private val kafkaEventProducer: KafkaEventProducer
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    suspend fun createOrder(order: Order): OrderResult {
        log.info { "Creating order for SKU: ${order.skuCode}" }

        return when (inventoryService.checkStock(order.skuCode, order.quantity)) {
            InventoryResult.InStock -> {
                val orderWithConfirmedStatus = order.copy(status = OrderStatusEnum.CONFIRMED)
                val savedOrder = orderDomainRepository.save(orderWithConfirmedStatus)
                
                kafkaEventProducer.publishOrderCompletedEvent(
                    OrderCompletedEvent.newBuilder()
                        .setOrderId(savedOrder.id.toString())
                        .setOrderDate(savedOrder.createdAt.toString())
                        .build()
                )
                
                OrderResult.Success(savedOrder)
            }

            InventoryResult.OutOfStock -> {
                OrderResult.BusinessFailure("Product ${order.skuCode} is out of stock")
            }
        }
    }
}