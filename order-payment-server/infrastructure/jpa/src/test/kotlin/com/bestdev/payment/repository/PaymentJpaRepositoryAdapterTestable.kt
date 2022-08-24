package com.bestdev.payment.repository

import com.bestdev.config.jpa.JpaConfig
import com.bestdev.config.mysql.BasicDataSourceConfig
import com.bestdev.repository.payment.PaymentJpaRepositoryAdapter
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        PaymentJpaRepositoryAdapter::class,
        PaymentRepository::class,
    ],
)
interface PaymentJpaRepositoryAdapterTestable
