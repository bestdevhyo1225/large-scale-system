package com.hyoseok.repository.follow

import com.hyoseok.entity.follow.FollowJpaEntity
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager")
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class FollowRepositoryImpl(
    private val followJpaRepository: FollowJpaRepository,
) : FollowRepository {

    override fun save(follow: Follow) {
        val followJpaEntity = FollowJpaEntity(follow = follow)
        followJpaRepository.save(followJpaEntity)
        followJpaEntity.mapDomainEntity(follow = follow)
    }
}
