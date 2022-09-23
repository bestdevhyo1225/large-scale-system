package com.hyoseok.config.feed

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisFeedEmbbededServerConfig(
    @Value("\${data.redis.feed.nodes}")
    private val nodes: List<String>,
) {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisFeedServer: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            val feedSplits: List<String> = nodes.first().split(":")
            embeddedRedisFeedServer = RedisServer(feedSplits[1].toInt())
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
