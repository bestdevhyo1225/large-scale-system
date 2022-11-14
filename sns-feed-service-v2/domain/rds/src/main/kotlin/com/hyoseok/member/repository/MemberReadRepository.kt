package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member

interface MemberReadRepository {
    fun findById(id: Long): Member
    fun findByInIdAndInfluencer(ids: List<Long>, influencer: Boolean): List<Member>
}
