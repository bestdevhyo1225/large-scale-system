package com.hyoseok.config.resilience4j

import com.hyoseok.constants.CircuitBreakerName
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.hibernate.exception.JDBCConnectionException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class Resilience4jConfig(
    @Value("\${resilience4j.circuitbreaker.configs.default.slidingWindowType}")
    private val slidingWindowType: SlidingWindowType,

    @Value("\${resilience4j.circuitbreaker.configs.default.slidingWindowSize}")
    private val slidingWindowSize: Int,

    @Value("\${resilience4j.circuitbreaker.configs.default.minimumNumberOfCalls}")
    private val minimumNumberOfCalls: Int,

    @Value("\${resilience4j.circuitbreaker.configs.default.failureRateThreshold}")
    private val failureRateThreshold: Float,

    @Value("\${resilience4j.circuitbreaker.configs.default.waitDurationInOpenState}")
    private val waitDurationInOpenState: Long,
) {

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        return CircuitBreakerRegistry.of(
            CircuitBreakerConfig.custom()
                .slidingWindowType(slidingWindowType)
                .slidingWindowSize(slidingWindowSize)
                .minimumNumberOfCalls(minimumNumberOfCalls)
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenState))
                .recordExceptions(JDBCConnectionException::class.java)
                .build(),
        )
    }

    @Bean
    fun mysqlCircuitBreaker(): CircuitBreaker = circuitBreakerRegistry().circuitBreaker(CircuitBreakerName.RDBMS_MYSQL)
}
