package com.hyoseok.message.repository

import com.hyoseok.message.entity.ReceiveMessageFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReceiveMessageFailLogJpaRepository : JpaRepository<ReceiveMessageFailLogEntity, Long>
