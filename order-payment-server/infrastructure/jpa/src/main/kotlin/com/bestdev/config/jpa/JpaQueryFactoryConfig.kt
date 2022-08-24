package com.bestdev.config.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class JpaQueryFactoryConfig {

    @PersistenceContext(unitName = "customEntityManager")
    private lateinit var entityManager: EntityManager

    @Bean
    fun jPAQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}
