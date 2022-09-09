package com.hyoseok.repository.sns

import com.hyoseok.entity.sns.SnsJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SnsJpaRepository : JpaRepository<SnsJpaEntity, Long>
