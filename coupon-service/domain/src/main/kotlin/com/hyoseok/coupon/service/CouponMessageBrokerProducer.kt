package com.hyoseok.coupon.service

interface CouponMessageBrokerProducer {
    fun <T : Any> send(event: T)
    fun <T : Any> sendAsync(event: T)
}
