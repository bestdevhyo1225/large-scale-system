package com.hyoseok.repository.sns

import com.hyoseok.repository.RepositoryImplTests
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.entity.SnsImage
import com.hyoseok.sns.entity.SnsTag
import com.hyoseok.sns.entity.enums.SnsTagType
import com.hyoseok.sns.repository.SnsRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
import com.hyoseok.utils.Sha256Util
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class SnsRepositoryImplTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var snsReadRepository: SnsReadRepository

    @Autowired
    private lateinit var snsRepository: SnsRepository

    init {
        this.describe("save 메서드는") {
            it("Sns, SnsImage, SnsTag 엔티티를 저장한다.") {
                // given
                val memberId = 1235L
                val title = "title"
                val contents = "contents"
                val writer = "writer"
                val productIds = listOf(1L, 2L, 3L)
                val snsImages = listOf(Pair("image0", 0), Pair("image1", 1), Pair("image2", 2))
                val tagType = "tpo"
                val tagValues = listOf("파티", "나들이")
                val sns = Sns(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    productIds = productIds,
                    snsImages = SnsImage.createSnsImages(snsImages = snsImages),
                    snsTag = SnsTag(type = tagType, values = tagValues),
                )

                // when
                snsRepository.save(sns = sns)

                // then
                val findSns = snsReadRepository.findWithAssociatedEntitiesById(snsId = sns.id!!)

                findSns.shouldBe(sns)
                findSns.snsImages.shouldNotBeEmpty()
                findSns.snsImages.forEachIndexed { index, snsImage ->
                    snsImage.id.shouldNotBeNull()
                    snsImage.url.shouldBe(Sha256Util.encode(value = snsImages[index].first))
                    snsImage.sortOrder.shouldBe(snsImages[index].second)
                }
                findSns.snsTag.shouldNotBeNull()
                findSns.snsTag!!.id.shouldNotBeNull()
                findSns.snsTag!!.type.shouldBe(SnsTagType(value = tagType))
                findSns.snsTag!!.values.containsAll(tagValues)
            }
        }

        this.describe("findById 메서드는") {
            it("Sns 엔티티만 조회한다.") {
                // given
                val memberId = 19823L
                val title = "title"
                val contents = "contents"
                val writer = "writer"
                val productIds = listOf(1L, 2L, 3L)
                val snsImages = listOf(Pair("image0", 0), Pair("image1", 1), Pair("image2", 2))
                val tagType = "tpo"
                val tagValues = listOf("파티", "나들이")
                val sns = Sns(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    productIds = productIds,
                    snsImages = SnsImage.createSnsImages(snsImages = snsImages),
                    snsTag = SnsTag(type = tagType, values = tagValues),
                )

                snsRepository.save(sns = sns)

                // when
                val findSns = snsReadRepository.findById(snsId = sns.id!!)

                // then
                findSns.shouldBe(sns)
                findSns.snsImages.shouldBeEmpty()
                findSns.snsTag.shouldBeNull()
            }
        }

        this.describe("findAllByLimitAndOffset 메서드는") {
            it("limit, offset 값을 통해 Sns 엔티티 리스트를 반환한다") {
                // given
                val memberIds = listOf(1L, 2L, 3L, 4L, 5L)
                val snsList = memberIds.map {
                    val title = "title"
                    val contents = "contents"
                    val writer = "writer"
                    val productIds = listOf(1L, 2L, 3L)
                    val snsImages = listOf(Pair("image0", 0), Pair("image1", 1), Pair("image2", 2))
                    val tagType = "tpo"
                    val tagValues = listOf("파티", "나들이")
                    Sns(
                        memberId = it,
                        title = title,
                        contents = contents,
                        writer = writer,
                        productIds = productIds,
                        snsImages = SnsImage.createSnsImages(snsImages = snsImages),
                        snsTag = SnsTag(type = tagType, values = tagValues),
                    )
                }

                snsList.forEach { snsRepository.save(sns = it) }

                // when
                val limit = 3L
                val offset = 0L
                val (findSnsList, totalCount) = snsReadRepository.findAllByLimitAndOffset(
                    limit = limit,
                    offset = offset,
                )

                // then
                findSnsList.shouldNotBeEmpty()
                findSnsList.shouldHaveSize(limit.toInt())
                findSnsList.forEachIndexed { index, sns ->
                    sns.snsImages.shouldNotBeEmpty()
                    sns.snsImages.shouldHaveSize(snsList[index].snsImages.size)
                    sns.snsTag.shouldNotBeNull()
                    sns.snsTag!!.type.shouldBe(snsList[index].snsTag!!.type)
                    sns.snsTag!!.values.containsAll(snsList[index].snsTag!!.values)
                }
                totalCount.shouldBe(snsList.size)
            }
        }
    }
}
