package com.hyoseok.member.repository

import com.hyoseok.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long>
