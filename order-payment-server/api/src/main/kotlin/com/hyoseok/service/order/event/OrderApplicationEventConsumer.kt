package com.hyoseok.service.order.event

import com.hyoseok.service.dto.event.CreatedOrderEventDto
import com.hyoseok.service.payment.PaymentExternalService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderApplicationEventConsumer(
    private val paymentExternalService: PaymentExternalService,
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: CreatedOrderEventDto) = runBlocking {
        launch(context = Dispatchers.IO) {
            paymentExternalService.request(merchantUniqueId = event.orderId)
        }
    }
}
