package com.hyoseok.wish.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.wish.dto.WishGroupByPostIdDto
import com.hyoseok.wish.entity.Wish
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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@DirtiesContext
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        WishRepository::class,
        WishReadRepository::class,
        WishReadRepositoryImpl::class,
    ],
)
internal class WishReadRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var wishRepository: WishRepository

    @Autowired
    private lateinit var wishReadRepository: WishReadRepository

    init {
        this.describe("countGroupByPostIds 메서드는") {
            it("postId를 기준으로 좋아요 횟수를 그룹화한 결과를 반환한다") {
                // given
                val postIds: List<Long> = listOf(1, 2, 3)
                val wishs: List<Wish> = listOf(
                    Wish(postId = postIds[0], memberId = 1),
                    Wish(postId = postIds[0], memberId = 2),
                    Wish(postId = postIds[0], memberId = 3),
                    Wish(postId = postIds[1], memberId = 1),
                    Wish(postId = postIds[2], memberId = 1),
                    Wish(postId = postIds[2], memberId = 2),
                )

                withContext(Dispatchers.IO) {
                    wishRepository.saveAll(wishs)
                }

                // when
                val wishGroupByPostIdDtos: List<WishGroupByPostIdDto> =
                    wishReadRepository.countGroupByPostIds(postIds = postIds)
                        .sortedBy { it.postId }

                // then
                wishGroupByPostIdDtos.shouldHaveSize(postIds.size)
                wishGroupByPostIdDtos[0].wishCount.shouldBe(3)
                wishGroupByPostIdDtos[1].wishCount.shouldBe(1)
                wishGroupByPostIdDtos[2].wishCount.shouldBe(2)
            }
        }
    }
}
