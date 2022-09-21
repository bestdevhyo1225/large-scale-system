package com.hyoseok.repository.member

import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class MemberRepositoryTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberReadRepository: MemberReadRepository

    init {
        this.describe("save 메서드는") {
            it("Member 엔티티를 저장한다") {
                // given
                val name = "Jang"
                val member = Member(name = name)

                // when
                memberRepository.save(member = member)

                // then
                member.id.shouldNotBeNull()
            }
        }

        this.describe("findById 메서드는") {
            it("Member 엔티티를 조회한다") {
                // given
                val name = "Jang"
                val member = Member(name = name)
                memberRepository.save(member = member)

                // when
                memberReadRepository.findById(id = member.id!!)
                    .shouldBe(member)
            }
        }
    }
}
