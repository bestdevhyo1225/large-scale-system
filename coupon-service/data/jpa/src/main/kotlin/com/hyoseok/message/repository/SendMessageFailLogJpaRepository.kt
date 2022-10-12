package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SendMessageFailLogJpaRepository : JpaRepository<SendMessageFailLogEntity, Long> {

    @Query("SELECT COUNT(smfl) FROM SendMessageFailLogEntity smfl")
    fun countAll(): Long

    @Query("SELECT COUNT(smfl) FROM SendMessageFailLogEntity smfl WHERE smfl.instanceId = :instanceId")
    fun countAllByInstanceId(instanceId: String): Long
}
