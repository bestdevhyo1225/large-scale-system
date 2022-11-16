package com.hyoseok.wish.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.wish.entity.WishEventLog
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
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
        WishEventLogRepository::class,
    ],
)
internal class WishEventLogRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var wishEventLogRepository: WishEventLogRepository

    init {
        this.describe("save 메서드는") {
            it("WishEventLog 엔티티를 저장한다") {
                // given
                val wishEventLog = WishEventLog(postId = 1, memberId = 1)

                // when
                withContext(Dispatchers.IO) {
                    wishEventLogRepository.save(wishEventLog)
                }

                // then
                wishEventLog.id.shouldNotBeNull()
            }
        }
    }
}
