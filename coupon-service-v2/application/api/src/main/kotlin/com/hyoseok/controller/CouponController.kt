package com.hyoseok.controller

import com.hyoseok.SuccessResponse
import com.hyoseok.controller.dto.CouponCreateRequestDto
import com.hyoseok.controller.dto.CouponIssuedCreateRequestDto
import com.hyoseok.coupon.dto.CouponDto
import com.hyoseok.coupon.dto.CouponIssuedCacheResultDto
import com.hyoseok.coupon.service.CouponService
import com.hyoseok.usecase.CreateCouponIssuedUsecase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
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
    private val createCouponIssuedUsecase: CreateCouponIssuedUsecase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody
        request: CouponCreateRequestDto,
    ): ResponseEntity<SuccessResponse<CouponDto>> {
        val couponDto: CouponDto = couponService.create(dto = request.toDto())
        return ResponseEntity
            .created(URI.create("/coupons/${couponDto.id}"))
            .body(SuccessResponse(data = couponDto))
    }

    @PostMapping("/{id}/issued")
    fun createCouponIssued(
        @PathVariable
        id: Long,
        @Valid @RequestBody
        request: CouponIssuedCreateRequestDto,
    ): ResponseEntity<SuccessResponse<CouponIssuedCacheResultDto>> {
        val couponIssuedCacheResultDto: CouponIssuedCacheResultDto =
            createCouponIssuedUsecase.execute(couponId = id, memberId = request.memberId)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SuccessResponse(data = couponIssuedCacheResultDto))
    }
}
