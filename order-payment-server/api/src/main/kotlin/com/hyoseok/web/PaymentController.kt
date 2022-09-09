package com.hyoseok.web

import com.hyoseok.service.dto.CreatePaymentResultDto
import com.hyoseok.service.payment.PaymentService
import com.hyoseok.web.request.CreatePaymentRequest
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    @PostMapping("/complete")
    fun completePayment(
        @Valid @RequestBody
        request: CreatePaymentRequest,
    ): ResponseEntity<SuccessResponse<CreatePaymentResultDto>> {
        val createPaymentResultDto: CreatePaymentResultDto = paymentService.create(dto = request.toServiceDto())
        return created(URI.create("/api/v1/payments/${createPaymentResultDto.id}"))
            .body(SuccessResponse(data = createPaymentResultDto))
    }
}
