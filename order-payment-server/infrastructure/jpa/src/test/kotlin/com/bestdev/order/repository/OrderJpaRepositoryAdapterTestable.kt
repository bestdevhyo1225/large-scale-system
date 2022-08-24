package com.bestdev.order.repository

import com.bestdev.config.jpa.JpaConfig
import com.bestdev.config.jpa.JpaQueryFactoryConfig
import com.bestdev.config.mysql.BasicDataSourceConfig
import com.bestdev.order.repository.read.OrderReadRepository
import com.bestdev.repository.order.OrderJpaRepositoryAdapter
import com.bestdev.repository.order.read.OrderReadJpaRepositoryAdapter
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        OrderRepository::class,
        OrderReadRepository::class,
        OrderJpaRepositoryAdapter::class,
        OrderReadJpaRepositoryAdapter::class,
    ],
)
internal interface OrderJpaRepositoryAdapterTestable
