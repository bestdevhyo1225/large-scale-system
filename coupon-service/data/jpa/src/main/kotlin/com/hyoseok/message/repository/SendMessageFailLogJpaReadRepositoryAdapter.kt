package com.hyoseok.message.repository

import com.hyoseok.message.entity.QSendMessageFailLogEntity.sendMessageFailLogEntity
import com.hyoseok.message.entity.SendMessageFailLog
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class SendMessageFailLogJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val sendMessageFailLogJpaRepository: SendMessageFailLogJpaRepository,
) : SendMessageFailLogReadRepository {

    override fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<SendMessageFailLog>> =
        Pair(
            first = sendMessageFailLogJpaRepository.countAll(),
            second = jpaQueryFactory
                .selectFrom(sendMessageFailLogEntity)
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    override fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<SendMessageFailLog>> =
        Pair(
            first = sendMessageFailLogJpaRepository.countAllByInstanceId(instanceId = instanceId),
            second = jpaQueryFactory
                .selectFrom(sendMessageFailLogEntity)
                .where(sendMessageFailLogEntityEqInstanceId(instanceId = instanceId))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    private fun sendMessageFailLogEntityEqInstanceId(instanceId: String): BooleanExpression =
        sendMessageFailLogEntity.instanceId.eq(instanceId)
}
