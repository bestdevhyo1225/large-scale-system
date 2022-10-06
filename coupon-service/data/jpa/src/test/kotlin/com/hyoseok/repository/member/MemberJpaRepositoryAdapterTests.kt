package com.hyoseok.repository.member

import com.hyoseok.JpaRepositoryAdapterTests
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class MemberJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        this.describe("save 메서드는") {
            it("Member 엔티티를 저장한다") {
                // given
                val name = "JangHyoSeok"
                val member = Member(name = name)

                // when
                memberRepository.save(member = member)

                // then
                member.id.shouldNotBeNull()
            }
        }
    }
}
