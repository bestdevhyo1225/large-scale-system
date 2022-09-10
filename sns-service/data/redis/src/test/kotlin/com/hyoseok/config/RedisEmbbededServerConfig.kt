package com.hyoseok.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisEmbbededServerConfig(
    @Value("\${spring.data.redis.port}")
    private val port: Int,
) {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisServer: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            embeddedRedisServer = RedisServer(port)
            embeddedRedisServer.start()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        try {
            embeddedRedisServer.stop()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }
}
