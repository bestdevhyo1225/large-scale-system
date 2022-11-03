package com.hyoseok.config.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
@EntityScan(basePackages = ["com.hyoseok.*.entity"])
@EnableJpaRepositories(basePackages = ["com.hyoseok.*.repository"])
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class JpaConfig {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun jPAQueryFactory(): JPAQueryFactory = JPAQueryFactory(entityManager)
}
