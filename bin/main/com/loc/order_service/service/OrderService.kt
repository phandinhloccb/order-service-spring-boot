package com.loc.order_service.service

import com.loc.order_service.enum.OrderStatusEnum
import com.loc.order_service.mapper.*
import com.loc.order_service.model.InventoryResult
import com.loc.order_service.model.Order
import com.loc.order_service.model.OrderResult
import com.loc.order_service.repository.OrderRepository
import com.loc.order_service.service.client.InventoryService
import com.loc.order_service.producer.KafkaEventProducer
import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val inventoryService: InventoryService,
    private val kafkaEventProducer: KafkaEventProducer
) {
    @Transactional
    suspend fun createOrder(order: Order): OrderResult {
        val log = KotlinLogging.logger {}
        log.info { "Creating order for SKU: ${order.skuCode}" }

        return when (inventoryService.checkStock(order.skuCode, order.quantity)) {
            InventoryResult.InStock -> {
                val orderWithConfirmedStatus = order.copy(status = OrderStatusEnum.CONFIRMED)
                val saved = withContext(Dispatchers.IO) {
                    orderRepository.save(orderWithConfirmedStatus.toEntity())
                }
                
                kafkaEventProducer.publishOrderCompletedEvent(
                    OrderCompletedEvent.newBuilder()
                        .setOrderId(saved.id.toString())
                        .setOrderDate(saved.createdAt.toString())
                        .build()
                )
                
                OrderResult.Success(saved.toModel())
            }

            InventoryResult.OutOfStock -> {
                OrderResult.BusinessFailure("Product ${order.skuCode} is out of stock")
            }
        }
    }
}