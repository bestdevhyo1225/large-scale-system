package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long>
