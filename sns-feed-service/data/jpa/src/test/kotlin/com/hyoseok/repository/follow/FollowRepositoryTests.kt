package com.hyoseok.repository.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class FollowRepositoryTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var followRepository: FollowRepository

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
    }
}
