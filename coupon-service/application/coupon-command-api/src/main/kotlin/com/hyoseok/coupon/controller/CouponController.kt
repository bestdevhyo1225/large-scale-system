package com.hyoseok.coupon.controller

import com.hyoseok.coupon.controller.request.CouponCreateRequest
import com.hyoseok.coupon.controller.request.CouponIssuedCreateRequest
import com.hyoseok.coupon.service.CouponIssuedService
import com.hyoseok.coupon.service.CouponService
import com.hyoseok.coupon.service.dto.CouponCreateResultDto
import com.hyoseok.coupon.service.dto.CouponIssuedCreateResultDto
import com.hyoseok.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/coupons")
class CouponController(
    private val couponService: CouponService,
    private val couponIssuedService: CouponIssuedService,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody
        request: CouponCreateRequest,
    ): ResponseEntity<SuccessResponse<CouponCreateResultDto>> {
        val couponCreateResultDto: CouponCreateResultDto = couponService.create(dto = request.toServiceDto())
        return ResponseEntity
            .created(URI.create("/coupons/${couponCreateResultDto.couponId}"))
            .body(SuccessResponse(data = couponCreateResultDto))
    }

    @PostMapping("/{id}/issued")
    fun createCouponIssued(
        @PathVariable
        id: Long,
        @Valid @RequestBody
        request: CouponIssuedCreateRequest,
    ): ResponseEntity<SuccessResponse<CouponIssuedCreateResultDto>> {
        val couponIssuedCreateResultDto: CouponIssuedCreateResultDto =
            couponIssuedService.create(dto = request.toServiceDto(couponId = id))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SuccessResponse(data = couponIssuedCreateResultDto))
    }
}
