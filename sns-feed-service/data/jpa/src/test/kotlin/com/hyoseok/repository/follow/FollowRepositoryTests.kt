package com.hyoseok.repository.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class FollowRepositoryTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var followRepository: FollowRepository

    @Autowired
    private lateinit var followReadRepository: FollowReadRepository

    init {
        this.describe("save 메서드는") {
            it("Follow 엔티티를 저장한다") {
                // given
                val followerId = 1523L
                val followeeId = 9281L
                val follow = Follow(followerId = followerId, followeeId = followeeId)

                // when
                followRepository.save(follow = follow)

                // then
                follow.id.shouldNotBeNull()
            }
        }

        this.describe("findAllByFolloweeIdAndLimitAndOffset 메서드는") {
            it("나를 팔로우한 사람들을 조회한다") {
                // given
                val myMemberId = 1841104L
                val myFolloweeMemberIds = listOf(11238L, 29283L, 92326L)
                val follows: List<Follow> = listOf(
                    Follow(followerId = myFolloweeMemberIds[0], followeeId = myMemberId),
                    Follow(followerId = myMemberId, followeeId = 1842L),
                    Follow(followerId = myFolloweeMemberIds[1], followeeId = myMemberId),
                    Follow(followerId = myFolloweeMemberIds[2], followeeId = myMemberId),
                    Follow(followerId = 19283L, followeeId = 12931L),
                )

                follows.forEach { followRepository.save(follow = it) }

                // when
                val limit = 3L
                val offset = 0L
                val pairFollow: Pair<Long, List<Follow>> = followReadRepository.findAllByFolloweeIdAndLimitAndOffset(
                    followeeId = myMemberId,
                    limit = limit,
                    offset = offset,
                )
                val findFollows: List<Follow> = pairFollow.second.sortedBy { it.id!! }

                // then
                pairFollow.first.shouldBe(3)
                findFollows[0].shouldBe(follows[0])
                findFollows[1].shouldBe(follows[2])
                findFollows[2].shouldBe(follows[3])
            }
        }
    }
}
