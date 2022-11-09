package com.hyoseok.wish.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.wish.entity.Wish
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
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
        WishRepository::class,
        WishReadRepository::class,
        WishReadRepositoryImpl::class,
    ],
)
internal class WishRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var wishRepository: WishRepository

    @Autowired
    private lateinit var wishReadRepository: WishReadRepository

    init {
        this.describe("save 메서드는") {
            it("Wish 엔티티를 저장한다") {
                // given
                val wish = Wish(postId = 1, memberId = 1)

                // when
                withContext(Dispatchers.IO) {
                    wishRepository.save(wish)
                }

                // then
                wishReadRepository.findBy(id = wish.id!!)
                    .shouldBe(wish)
            }
        }
    }
}
