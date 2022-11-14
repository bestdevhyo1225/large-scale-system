package com.hyoseok.member.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.member.entity.Member
import com.hyoseok.member.entity.Member.Companion.NUMBER_OF_INFLUENCER
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        MemberRepository::class,
        MemberReadRepository::class,
        MemberReadRepositoryImpl::class,
    ],
)
internal class MemberRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberReadRepository: MemberReadRepository

    init {
        this.afterSpec {
            withContext(Dispatchers.IO) {
                memberRepository.deleteAll()
            }
        }

        this.describe("save 메서드는") {
            it("Member 엔티티를 저장한다") {
                // given
                val member = Member(name = "test")

                // when
                withContext(Dispatchers.IO) {
                    memberRepository.save(member)
                }

                // then
                val findMember: Member = memberReadRepository.findById(member.id!!)

                findMember.shouldBe(member)
            }
        }

        this.describe("findByInIdAndInfluencer 메서드는") {
            context("influencer 값이 true인 경우") {
                it("Member 엔티티 리스트를 조회한다") {
                    // given
                    val normalMembers: List<Member> = (1..5).map { Member(name = "normal-$it") }
                    val influencerMembers: List<Member> = (1..5).map {
                        val member = Member(name = "influencer-$it")
                        member.switchInfluencerAccount(followerCount = NUMBER_OF_INFLUENCER.plus(other = 1))
                        member
                    }
                    val members: List<Member> = normalMembers.plus(influencerMembers)

                    withContext(Dispatchers.IO) {
                        memberRepository.saveAll(members)
                    }

                    // when
                    val findMembers: List<Member> =
                        memberReadRepository.findByInIdAndInfluencer(ids = members.map { it.id!! }, influencer = true)

                    // then
                    findMembers.shouldNotBeEmpty()
                    findMembers.shouldHaveSize(influencerMembers.size)
                }
            }
        }
    }
}
