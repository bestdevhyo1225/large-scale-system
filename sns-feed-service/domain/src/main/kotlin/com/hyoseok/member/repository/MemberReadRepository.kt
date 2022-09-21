package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member

interface MemberReadRepository {

    fun exists(id: Long): Boolean
    fun findById(id: Long): Member
}
