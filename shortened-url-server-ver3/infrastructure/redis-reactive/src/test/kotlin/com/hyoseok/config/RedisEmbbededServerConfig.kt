package com.hyoseok.config

import com.hyoseok.config.RedisNodesKey.SERVER_1
import com.hyoseok.config.RedisNodesKey.SERVER_2
import com.hyoseok.config.RedisNodesKey.SERVER_3
import com.hyoseok.config.standalone.property.RedisServers
import mu.KotlinLogging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import redis.embedded.exceptions.EmbeddedRedisException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(value = [RedisServers::class])
class RedisEmbbededServerConfig(
    private val redisServers: RedisServers,
) {

    private val logger = KotlinLogging.logger {}

    private lateinit var embeddedRedisServer1: RedisServer
    private lateinit var embeddedRedisServer2: RedisServer
    private lateinit var embeddedRedisServer3: RedisServer

    @PostConstruct
    fun startEmbeddedRedisServer() {
        try {
            val splitServer1Nodes = redisServers.nodes[SERVER_1]!!.first().split(":")
            val splitServer2Nodes = redisServers.nodes[SERVER_2]!!.first().split(":")
            val splitServer3Nodes = redisServers.nodes[SERVER_3]!!.first().split(":")

            embeddedRedisServer1 = RedisServer(splitServer1Nodes[1].toInt())
            embeddedRedisServer2 = RedisServer(splitServer2Nodes[1].toInt())
            embeddedRedisServer3 = RedisServer(splitServer3Nodes[1].toInt())

            embeddedRedisServer1.start()
            embeddedRedisServer2.start()
            embeddedRedisServer3.start()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }

    @PreDestroy
    fun stopEmbeddedRedisServer() {
        try {
            embeddedRedisServer1.stop()
            embeddedRedisServer2.stop()
            embeddedRedisServer3.stop()
        } catch (exception: EmbeddedRedisException) {
            logger.error { exception }
        }
    }
}
