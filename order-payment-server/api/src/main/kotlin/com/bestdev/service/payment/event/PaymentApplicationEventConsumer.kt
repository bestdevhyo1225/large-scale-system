package com.bestdev.service.payment.event

import com.bestdev.service.dto.CreateSubInfoOfOrderDto
import com.bestdev.service.dto.event.CreatedPaymentEventDto
import com.bestdev.service.order.OrderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentApplicationEventConsumer(
    private val orderService: OrderService,
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: CreatedPaymentEventDto) = runBlocking {
        launch(context = Dispatchers.IO) {
            orderService.createOrderShipping(dto = CreateSubInfoOfOrderDto(orderId = event.orderId))
        }
    }
}
