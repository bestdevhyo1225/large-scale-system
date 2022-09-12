package com.hyoseok.repository.sns

import com.hyoseok.entity.sns.SnsJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface SnsJpaRepository : JpaRepository<SnsJpaEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SnsJpaEntity s SET s.deletedAt = :deletedAt WHERE s.id = :id")
    fun updateDeletedAt(deletedAt: LocalDateTime, id: Long)

    @Query("SELECT COUNT(s) FROM SnsJpaEntity s WHERE s.deletedAt IS NULL")
    fun countExcludeDeletedSnsJpaEntity(): Long
}
