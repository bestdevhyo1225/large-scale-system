package com.hyoseok.config.post

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class RedisPostEmbbededServerConfig(
    @Value("\${data.redis.post.nodes}")
    private val nodes: List<String>,
) {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisPostServer: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            val postSplits: List<String> = nodes.first().split(":")
            embeddedRedisPostServer = RedisServer(postSplits[1].toInt())
            embeddedRedisPostServer.start()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        try {
            embeddedRedisPostServer.stop()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }
}
