package com.hyoseok.config.jpa

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class JpaQueryFactoryConfig {

    @PersistenceContext(unitName = "customEntityManager")
    private lateinit var entityManager: EntityManager

    @Bean
    fun jPAQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}
