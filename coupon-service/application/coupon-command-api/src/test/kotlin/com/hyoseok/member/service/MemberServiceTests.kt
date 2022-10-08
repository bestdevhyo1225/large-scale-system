package com.hyoseok.member.service

import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.member.service.MemberService
import com.hyoseok.member.service.dto.MemberCreateDto
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class MemberServiceTests : DescribeSpec(
    {
        val mockMemberRepository: MemberRepository = mockk()
        val memberService = MemberService(memberRepository = mockMemberRepository)

        describe("create 메서드는") {
            it("회원을 생성한다") {
                // given
                val dto = MemberCreateDto(name = "Jang")

                every { mockMemberRepository.save(member = any()) } returns Unit

                // when
                withContext(Dispatchers.IO) {
                    memberService.create(dto = dto)
                }

                // then
                verify(exactly = 1) { mockMemberRepository.save(member = any()) }
            }
        }
    },
)
