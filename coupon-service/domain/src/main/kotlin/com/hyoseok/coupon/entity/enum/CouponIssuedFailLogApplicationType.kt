package com.hyoseok.coupon.entity.enum

enum class CouponIssuedFailLogApplicationType {
    PRODUCER,
    CONSUMER,
    ;

    companion object {
        operator fun invoke(value: String): CouponIssuedFailLogApplicationType = valueOf(value.uppercase())
    }
}
