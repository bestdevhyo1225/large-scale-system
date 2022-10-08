package com.hyoseok

import com.hyoseok.config.datasource.DataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.config.jpa.JpaQueryFactoryConfig
import com.hyoseok.repository.coupon.CouponIssuedFailJpaRepository
import com.hyoseok.repository.coupon.CouponIssuedFailJpaRepositoryAdapter
import com.hyoseok.repository.coupon.CouponIssuedJpaRepository
import com.hyoseok.repository.coupon.CouponIssuedJpaRepositoryAdapter
import com.hyoseok.repository.coupon.CouponJpaRepository
import com.hyoseok.repository.coupon.CouponJpaRepositoryAdapter
import com.hyoseok.repository.coupon.CouponJpaReadRepositoryAdapter
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
        CouponIssuedFailJpaRepository::class,
        MemberJpaRepository::class,
        CouponJpaRepositoryAdapter::class,
        CouponJpaReadRepositoryAdapter::class,
        CouponIssuedJpaRepositoryAdapter::class,
        CouponIssuedFailJpaRepositoryAdapter::class,
        MemberJpaRepositoryAdapter::class,
    ],
)
interface JpaRepositoryAdapterTests
