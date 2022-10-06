package com.hyoseok

import com.hyoseok.config.datasource.DataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.repository.coupon.CouponIssuedJpaRepository
import com.hyoseok.repository.coupon.CouponIssuedJpaRepositoryAdapter
import com.hyoseok.repository.coupon.CouponJpaRepository
import com.hyoseok.repository.coupon.CouponJpaRepositoryAdapter
import com.hyoseok.repository.member.MemberJpaRepository
import com.hyoseok.repository.member.MemberJpaRepositoryAdapter
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        DataSourceConfig::class,
        JpaConfig::class,
        JpaQueryFactoryConfig::class,
        CouponJpaRepository::class,
        CouponIssuedJpaRepository::class,
        MemberJpaRepository::class,
        CouponJpaRepositoryAdapter::class,
        CouponIssuedJpaRepositoryAdapter::class,
        MemberJpaRepositoryAdapter::class,
    ],
)
interface JpaRepositoryAdapterTests
