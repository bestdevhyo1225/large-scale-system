package com.bestdev.web

import com.bestdev.service.dto.CreateOrderResultDto
import com.bestdev.service.order.OrderService
import com.bestdev.web.request.CreateOrderRequest
import com.bestdev.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody
        request: CreateOrderRequest,
    ): ResponseEntity<SuccessResponse<CreateOrderResultDto>> {
        val createOrderResultDto: CreateOrderResultDto = orderService.create(dto = request.toServiceDto())
        return created(URI.create("/api/v1/orders/${createOrderResultDto.id}"))
            .body(SuccessResponse(data = createOrderResultDto))
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam(value = "status") status: String,
    ) {
        orderService.updateStatus(id = id, status = status)
    }
}
