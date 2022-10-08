package com.hyoseok.config

import com.hyoseok.CouponCommandApiApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(classes = [CouponCommandApiApplication::class, RedisCouponEmbbededServerConfig::class])
@DirtiesContext
interface IntegrationTests
