package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLog

interface SendMessageFailLogReadRepository {
    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<SendMessageFailLog>>
    fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<SendMessageFailLog>>
}
