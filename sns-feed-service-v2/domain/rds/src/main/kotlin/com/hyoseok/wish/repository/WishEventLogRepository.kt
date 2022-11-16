package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.WishEventLog
import org.springframework.data.jpa.repository.JpaRepository

interface WishEventLogRepository : JpaRepository<WishEventLog, Long>
