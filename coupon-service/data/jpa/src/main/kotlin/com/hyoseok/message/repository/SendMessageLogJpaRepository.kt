package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SendMessageLogJpaRepository : JpaRepository<SendMessageLogEntity, Long> {

    @Query("SELECT COUNT(sml) FROM SendMessageLogEntity sml")
    fun countAll(): Long

    @Query("SELECT COUNT(sml) FROM SendMessageLogEntity sml WHERE sml.instanceId = :instanceId")
    fun countAllByInstanceId(instanceId: String): Long
}
