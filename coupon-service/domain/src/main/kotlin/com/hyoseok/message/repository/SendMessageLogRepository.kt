package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageLog

interface SendMessageLogRepository {
    fun save(sendMessageLog: SendMessageLog)
}
