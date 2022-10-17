package com.hyoseok.message.repository

import com.hyoseok.message.entity.ReceiveMessageFailLog
import com.hyoseok.message.entity.ReceiveMessageFailLogEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ReceiveMessageFailLogJpaRepositoryAdapter(
    private val receiveMessageFailLogJpaRepository: ReceiveMessageFailLogJpaRepository,
) : ReceiveMessageFailLogRepository {

    override fun save(receiveMessageFailLog: ReceiveMessageFailLog) {
        receiveMessageFailLogJpaRepository
            .save(ReceiveMessageFailLogEntity(receiveMessageFailLog = receiveMessageFailLog))
            .also { receiveMessageFailLog.changeId(id = it.id!!) }
    }
}
