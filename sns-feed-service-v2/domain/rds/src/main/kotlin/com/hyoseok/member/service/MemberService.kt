package com.hyoseok.member.service

import com.hyoseok.member.dto.MemberCreateDto
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberReadRepositoryImpl.ErrorMessage.NOT_FOUND_MEMBER
import com.hyoseok.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/*
* 도메인 비즈니스
* - 도메인 단위에서 생성/변경/소멸의 라이프 사이클을 가지면 도메인 비즈니스이다.
* */
@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun create(dto: MemberCreateDto): MemberDto =
        Member(name = dto.name)
            .run { memberRepository.save(this) }
            .let {
                with(receiver = it) {
                    MemberDto(
                        id = id!!,
                        name = name,
                        influencer = getInfluencer(),
                        createdAt = createdAt,
                        lastLoginDatetime = lastLoginDatetime,
                    )
                }
            }

    fun updateInfluener(memberId: Long) {
        memberRepository.findByIdOrNull(id = memberId)
            ?.changeInfluencer(influencer = true) ?: throw NoSuchElementException(NOT_FOUND_MEMBER)
    }

    fun updateLastLoginDatetime(memberId: Long) {
        memberRepository.findByIdOrNull(id = memberId)
            ?.changeLastLoginDatetime(lastLoginDatetime = LocalDateTime.now().withNano(0))
            ?: throw NoSuchElementException(NOT_FOUND_MEMBER)
    }
}
