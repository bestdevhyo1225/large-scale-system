package com.hyoseok.repository

import com.hyoseok.config.BasicConnectionConfig
import com.hyoseok.entity.Url
import com.hyoseok.entity.UrlEntity
import com.hyoseok.utils.Sha256Util
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@DataR2dbcTest
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        BasicConnectionConfig::class,
        UrlCoroutineRepository::class,
        UrlReactiveRepository::class,
        UrlNonBlockingRepositoryAdapter::class,
    ],
)
internal class UrlNonBlockingRepositoryAdapterTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var urlCoroutineRepository: UrlCoroutineRepository

    @Autowired
    private lateinit var urlNonBlockingRepositoryAdapter: UrlNonBlockingRepositoryAdapter

    init {
        this.describe("findByEncodedUrl 메서드는") {
            it("longUrl 값을 반환한다.") {
                // given
                val longUrl = "https://test.com"
                val encodedUrl = Sha256Util.encode(value = longUrl)
                val urlEntity =
                    UrlEntity(encodedUrl = encodedUrl, longUrl = longUrl, createdAt = LocalDateTime.now().withNano(0))

                urlCoroutineRepository.save(urlEntity)

                // when
                val url: Url? = urlNonBlockingRepositoryAdapter.findByEncodedUrl(encodedUrl = encodedUrl)

                // then
                url.shouldNotBeNull()
                url.id.shouldNotBeNull()
                url.encodedUrl.shouldBe(urlEntity.encodedUrl)
                url.longUrl.shouldBe(urlEntity.longUrl)
                url.createdAt.shouldBe(urlEntity.createdAt)
            }
        }
    }
}
