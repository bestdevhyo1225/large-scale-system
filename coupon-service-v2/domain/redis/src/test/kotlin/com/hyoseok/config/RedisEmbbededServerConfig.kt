package com.hyoseok.config

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisEmbbededServerConfig {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisFeedServer: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            embeddedRedisFeedServer = RedisServer(6379)
            embeddedRedisFeedServer.start()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        try {
            embeddedRedisFeedServer.stop()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }
}
