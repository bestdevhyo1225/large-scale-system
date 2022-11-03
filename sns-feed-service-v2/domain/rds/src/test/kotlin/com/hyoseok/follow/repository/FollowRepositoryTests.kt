package com.hyoseok.follow.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.follow.entity.Follow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
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
        FollowRepository::class,
        FollowReadRepository::class,
        FollowReadRepositoryImpl::class,
    ],
)
internal class FollowRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var followRepository: FollowRepository

    @Autowired
    private lateinit var followReadRepository: FollowReadRepository

    init {
        this.afterSpec {
            withContext(Dispatchers.IO) {
                followRepository.deleteAll()
            }
        }

        this.describe("save 메서드는") {
            it("Follow 엔티티를 저장한다") {
                // given
                val follow = Follow(followerId = 1L, followeeId = 2L)

                // when
                withContext(Dispatchers.IO) {
                    followRepository.save(follow)
                }

                // then
                val findFollow = followReadRepository.findById(id = follow.id!!)

                findFollow.shouldBe(follow)
            }
        }

        this.describe("findAllByFollowerIdAndLimitAndOffset 메서드는") {
            it("내가 팔로우한 Follow 엔티티 리스트를 조회한다") {
                // given
                val limit = 5L
                val offset = 0L
                val followerId = 1L
                val follows: List<Follow> = (2L..11L).map { Follow(followerId = followerId, followeeId = it) }

                withContext(Dispatchers.IO) {
                    followRepository.saveAll(follows)
                }

                // when
                val result: Pair<Long, List<Follow>> = followReadRepository.findAllByFollowerIdAndLimitAndOffset(
                    followerId = followerId,
                    limit = limit,
                    offset = offset,
                )

                // then
                result.first.shouldBe(follows.size)
                result.second.shouldHaveSize(limit.toInt())
            }
        }
    }
}
