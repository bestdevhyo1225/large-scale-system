package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLog

interface SendMessageFailLogRepository {
    fun save(sendMessageFailLog: SendMessageFailLog)
}
