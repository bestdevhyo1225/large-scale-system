package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLog
import com.hyoseok.message.entity.SendMessageFailLogEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class SendMessageFailLogJpaRepositoryAdapter(
    private val sendMessageFailLogJpaRepository: SendMessageFailLogJpaRepository,
) : SendMessageFailLogRepository {

    override fun save(sendMessageFailLog: SendMessageFailLog) {
        val sendMessageFailLogEntity = SendMessageFailLogEntity(sendMessageFailLog = sendMessageFailLog)
        sendMessageFailLogJpaRepository.save(sendMessageFailLogEntity)
        sendMessageFailLog.changeId(id = sendMessageFailLogEntity.id!!)
    }
}
