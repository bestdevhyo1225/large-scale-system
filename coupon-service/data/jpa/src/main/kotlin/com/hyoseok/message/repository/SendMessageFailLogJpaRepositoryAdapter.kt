package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLog
import com.hyoseok.message.entity.SendMessageFailLogEntity
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager")
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class SendMessageFailLogJpaRepositoryAdapter(
    private val sendMessageFailLogJpaRepository: SendMessageFailLogJpaRepository,
) : SendMessageFailLogRepository {

    override fun save(sendMessageFailLog: SendMessageFailLog) {
        sendMessageFailLogJpaRepository.save(SendMessageFailLogEntity(sendMessageFailLog = sendMessageFailLog))
            .also { sendMessageFailLog.changeId(id = it.id!!) }
    }
}
