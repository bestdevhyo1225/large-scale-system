package com.hyoseok

import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.config.mysql.BasicDataSourceConfig
import com.hyoseok.order.repository.OrderRepository
import com.hyoseok.order.repository.read.OrderReadRepository
import com.hyoseok.payment.repository.PaymentRepository
import com.hyoseok.repository.order.OrderJpaRepositoryAdapter
import com.hyoseok.repository.order.read.OrderReadJpaRepositoryAdapter
import com.hyoseok.repository.payment.PaymentJpaRepositoryAdapter
import com.hyoseok.repository.shipping.ShippingJpaRepositoryAdapter
import com.hyoseok.shipping.repository.ShippingRepository
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
