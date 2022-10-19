package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageLog
import com.hyoseok.message.entity.SendMessageLogEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class SendMessageLogJpaRepositoryAdapter(
    private val sendMessageLogJpaRepository: SendMessageLogJpaRepository,
) : SendMessageLogRepository {

    override fun save(sendMessageLog: SendMessageLog) {
        sendMessageLogJpaRepository.save(SendMessageLogEntity(sendMessageLog = sendMessageLog))
            .also { sendMessageLog.changeId(id = it.id!!) }
    }
}
