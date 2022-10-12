package com.hyoseok.message.repository

import com.hyoseok.message.entity.SendMessageFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SendMessageFailLogJpaRepository : JpaRepository<SendMessageFailLogEntity, Long>
