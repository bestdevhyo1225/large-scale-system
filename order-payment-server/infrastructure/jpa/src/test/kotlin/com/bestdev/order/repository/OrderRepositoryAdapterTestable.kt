package com.bestdev.order.repository

import com.bestdev.config.jpa.JpaConfig
import com.bestdev.config.mysql.BasicDataSourceConfig
import com.bestdev.repository.order.OrderJpaRepositoryAdapter
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        OrderJpaRepositoryAdapter::class,
        OrderRepository::class,
    ],
)
internal interface OrderRepositoryAdapterTestable
