package com.hyoseok.service.dto

import com.hyoseok.member.entity.Member

data class MemberCreateDto(
    val name: String,
) {
    fun toEntity() =
        Member(name = name)
}

data class MemberCreateResultDto(
    val memberId: Long,
) {
    companion object {
        operator fun invoke(member: Member) =
            with(receiver = member) {
                MemberCreateResultDto(memberId = id!!)
            }
    }
}

data class MemberFindResultDto(
    val id: Long,
    val name: String,
) {
    companion object {
        operator fun invoke(member: Member) =
            with(receiver = member) {
                MemberFindResultDto(id = id!!, name = name)
            }
    }
}
