package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member

interface MemberRepository {
    fun save(member: Member)
}
