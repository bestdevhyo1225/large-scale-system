package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageLog

interface SendMessageLogReadRepository {
    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<SendMessageLog>>
    fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<SendMessageLog>>
}
