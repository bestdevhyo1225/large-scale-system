package com.hyoseok.member.service

import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.repository.MemberReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadService(
    private val memberReadRepository: MemberReadRepository,
) {
    fun findMember(id: Long): MemberDto =
        with(receiver = memberReadRepository.findById(id = id)) {
            MemberDto(id = id, name = name, influencer = influencer, createdAt = createdAt)
        }

    fun findInfluencerMembers(ids: List<Long>): List<MemberDto> {
        if (ids.isEmpty()) {
            return listOf()
        }

        return memberReadRepository.findByInIdAndInfluencer(ids = ids, influencer = true)
            .map {
                with(receiver = it) {
                    MemberDto(id = id!!, name = name, influencer = influencer, createdAt = createdAt)
                }
            }
    }
}
