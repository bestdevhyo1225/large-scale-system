package com.hyoseok.coupon.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class RedisCouponEmbbededServerConfig(
    @Value("\${data.redis.coupon.nodes}")
    private val nodes: List<String>,
) {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisCouponServer: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            val nodesSplits: List<String> = nodes.first().split(":")
            embeddedRedisCouponServer = RedisServer(nodesSplits[1].toInt())
            embeddedRedisCouponServer.start()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        try {
            embeddedRedisCouponServer.stop()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }
}
