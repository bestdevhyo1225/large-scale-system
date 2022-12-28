package com.hyoseok.member.dto

import java.time.LocalDateTime

data class MemberDto(
    val id: Long,
    val name: String,
    val influencer: Boolean,
    val createdAt: LocalDateTime,
    val lastLoginDatetime: LocalDateTime,
)

data class MemberCreateDto(
    val name: String,
)

data class MemberUpdateDto(
    val id: Long,
    val name: String,
)
