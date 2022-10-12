package com.hyoseok.message.repository

import com.hyoseok.message.entity.ReceiveMessageFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReceiveMessageFailLogJpaRepository : JpaRepository<ReceiveMessageFailLogEntity, Long> {

    @Query("SELECT COUNT(rmfl) FROM ReceiveMessageFailLogEntity rmfl")
    fun countAll(): Long

    @Query("SELECT COUNT(rmfl) FROM ReceiveMessageFailLogEntity rmfl WHERE rmfl.instanceId = :instanceId")
    fun countAllByInstanceId(instanceId: String): Long
}
