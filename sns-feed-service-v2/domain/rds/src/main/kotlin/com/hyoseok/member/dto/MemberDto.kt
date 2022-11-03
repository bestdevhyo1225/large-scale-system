package com.hyoseok.member.dto

import java.time.LocalDateTime

data class MemberDto(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
)

data class MemberCreateDto(
    val name: String,
)

data class MemberUpdateDto(
    val id: Long,
    val name: String,
)