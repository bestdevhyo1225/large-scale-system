package com.hyoseok.config

import com.hyoseok.config.RedisNodesKey.SERVER_1
import com.hyoseok.config.RedisNodesKey.SERVER_2
import com.hyoseok.config.RedisNodesKey.SERVER_3
import com.hyoseok.config.property.RedisServers
import io.lettuce.core.ReadFrom
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration

object RedisCommonConfig {

    fun getHostAndPort(redisServers: RedisServers, redisNodesKey: String): Pair<String, Int> {
        val splitNodes = when (redisNodesKey) {
            SERVER_1 -> redisServers.nodes[SERVER_1]!!.first().split(":")
            SERVER_2 -> redisServers.nodes[SERVER_2]!!.first().split(":")
            SERVER_3 -> redisServers.nodes[SERVER_3]!!.first().split(":")
            else -> throw IllegalArgumentException("redisNodesKey 값을 다시 확인하세요")
        }

        return with(receiver = splitNodes) { Pair(first = this[0], second = this[1].toInt()) }
    }

    fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("large-scale-system-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()
}
