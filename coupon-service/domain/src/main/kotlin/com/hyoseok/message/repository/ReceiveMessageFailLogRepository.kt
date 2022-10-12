package com.hyoseok.message.repository

import com.hyoseok.message.entity.ReceiveMessageFailLog

interface ReceiveMessageFailLogRepository {
    fun save(receiveMessageFailLog: ReceiveMessageFailLog)
}
