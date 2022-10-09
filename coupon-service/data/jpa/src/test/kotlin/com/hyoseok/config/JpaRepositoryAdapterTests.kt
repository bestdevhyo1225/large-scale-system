package com.hyoseok.config

import com.hyoseok.config.datasource.DataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.coupon.repository.CouponIssuedFailJpaRepository
import com.hyoseok.coupon.repository.CouponIssuedFailJpaRepositoryAdapter
import com.hyoseok.coupon.repository.CouponIssuedJpaReadRepositoryAdapter
import com.hyoseok.coupon.repository.CouponIssuedJpaRepository
import com.hyoseok.coupon.repository.CouponIssuedJpaRepositoryAdapter
import com.hyoseok.coupon.repository.CouponJpaRepository
import com.hyoseok.coupon.repository.CouponJpaRepositoryAdapter
import com.hyoseok.coupon.repository.CouponJpaReadRepositoryAdapter
import com.hyoseok.member.repository.MemberJpaRepository
import com.hyoseok.member.repository.MemberJpaRepositoryAdapter
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
        CouponIssuedFailJpaRepository::class,
        MemberJpaRepository::class,
        CouponJpaReadRepositoryAdapter::class,
        CouponJpaRepositoryAdapter::class,
        CouponIssuedJpaReadRepositoryAdapter::class,
        CouponIssuedJpaRepositoryAdapter::class,
        CouponIssuedFailJpaRepositoryAdapter::class,
        MemberJpaRepositoryAdapter::class,
    ],
)
interface JpaRepositoryAdapterTests
