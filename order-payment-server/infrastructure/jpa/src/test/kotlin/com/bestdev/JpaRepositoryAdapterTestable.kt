package com.bestdev

import com.bestdev.config.jpa.JpaConfig
import com.bestdev.config.jpa.JpaQueryFactoryConfig
import com.bestdev.config.mysql.BasicDataSourceConfig
import com.bestdev.order.repository.OrderRepository
import com.bestdev.order.repository.read.OrderReadRepository
import com.bestdev.payment.repository.PaymentRepository
import com.bestdev.repository.order.OrderJpaRepositoryAdapter
import com.bestdev.repository.order.read.OrderReadJpaRepositoryAdapter
import com.bestdev.repository.payment.PaymentJpaRepositoryAdapter
import com.bestdev.repository.shipping.ShippingJpaRepositoryAdapter
import com.bestdev.shipping.repository.ShippingRepository
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        // Order
        OrderRepository::class,
        OrderReadRepository::class,
        OrderJpaRepositoryAdapter::class,
        OrderReadJpaRepositoryAdapter::class,
        // Payment
        PaymentRepository::class,
        PaymentJpaRepositoryAdapter::class,
        // Shipping
        ShippingRepository::class,
        ShippingJpaRepositoryAdapter::class,
    ],
)
interface JpaRepositoryAdapterTestable
