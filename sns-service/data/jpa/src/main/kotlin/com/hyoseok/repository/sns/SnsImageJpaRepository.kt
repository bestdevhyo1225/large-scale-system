package com.hyoseok.repository.sns

import com.hyoseok.entity.sns.SnsImageJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface SnsImageJpaRepository : JpaRepository<SnsImageJpaEntity, Long> {

    @Modifying
    @Query("DELETE FROM SnsImageJpaEntity si WHERE si.id IN (:ids)")
    fun deleteAllByIds(ids: List<Long>)
}
