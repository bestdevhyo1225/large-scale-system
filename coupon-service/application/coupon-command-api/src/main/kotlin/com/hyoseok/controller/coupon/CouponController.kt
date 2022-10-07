package com.hyoseok.controller.coupon

import com.hyoseok.controller.request.CouponCreateRequest
import com.hyoseok.service.coupon.CouponService
import com.hyoseok.service.dto.CouponCreateResultDto
import com.hyoseok.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/coupons")
class CouponController(
    private val couponService: CouponService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: CouponCreateRequest,
    ): ResponseEntity<SuccessResponse<CouponCreateResultDto>> {
        val couponCreateResultDto: CouponCreateResultDto = couponService.create(dto = request.toServiceDto())
        return ResponseEntity.created(URI.create("/coupons/${couponCreateResultDto.couponId}"))
            .body(SuccessResponse(data = couponCreateResultDto))
    }
}
