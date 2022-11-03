package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member

interface MemberReadRepository {
    fun findById(id: Long): Member
}
