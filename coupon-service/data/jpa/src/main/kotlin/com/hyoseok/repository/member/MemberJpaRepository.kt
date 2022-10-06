package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long>
