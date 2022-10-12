package com.hyoseok.message.repository

import com.hyoseok.message.entity.ReceiveMessageFailLog

interface ReceiveMessageFailLogReadRepository {
    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<ReceiveMessageFailLog>>
    fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<ReceiveMessageFailLog>>
}
