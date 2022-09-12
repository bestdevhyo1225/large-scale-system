package com.hyoseok.repository.sns

import com.hyoseok.exception.Message.NOT_FOUND_SNS
import com.hyoseok.repository.RepositoryImplTests
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.entity.SnsImage
import com.hyoseok.sns.entity.SnsTag
import com.hyoseok.sns.entity.enums.SnsTagType
import com.hyoseok.sns.repository.SnsRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
import com.hyoseok.utils.Sha256Util
import io.kotest.assertions.throwables.shouldThrow
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
import javax.persistence.EntityManager

internal class SnsRepositoryImplTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var snsReadRepository: SnsReadRepository

    @Autowired
    private lateinit var snsRepository: SnsRepository

    @Autowired
    private lateinit var entityManager: EntityManager

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

                sns.snsImages.forEach { it.id.shouldNotBeNull() }
                sns.snsTag!!.id.shouldNotBeNull()
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

            it("삭제된 Sns 엔티티는 조회하지 않는다") {
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
                snsRepository.delete(id = sns.id!!)

                // when
                val exception = shouldThrow<NoSuchElementException> { snsReadRepository.findById(snsId = sns.id!!) }

                // then
                exception.localizedMessage.shouldBe(NOT_FOUND_SNS)
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

            it("삭제되지 않은 Sns 엔티티 리스트를 조회한다") {
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
                val deletedSnsList: List<Sns> = snsList
                    .filter { it.id!! < 3L }
                    .map {
                        snsRepository.delete(id = it.id!!)
                        it
                    }

                // when
                val limit = 2L
                val offset = 0L
                val (findSnsList, totalCount) = snsReadRepository.findAllByLimitAndOffset(
                    limit = limit,
                    offset = offset,
                )

                // then
                findSnsList.shouldNotBeEmpty()
                findSnsList.shouldHaveSize(limit.toInt())
                totalCount.shouldBe(snsList.size.minus(deletedSnsList.size))
            }
        }

        this.describe("update 메서드는") {
            it("Sns, SnsImage, SnsTag 엔티티를 수정한다") {
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

                val updateTitle = "updateTitle"
                val updateContents = "updateContents"
                val updateProductIds = listOf(1L, 2L)
                val updateSnsImages = listOf(Pair("image0", 0), Pair("image3", 1))
                val updateTagType = "style"
                val updateTagValues = listOf("파티", "출근")

                val updateSns = Sns(
                    id = sns.id!!,
                    memberId = memberId,
                    title = updateTitle,
                    contents = updateContents,
                    writer = writer,
                    productIds = updateProductIds,
                    snsImages = SnsImage.createSnsImages(snsImages = updateSnsImages),
                    snsTag = SnsTag(id = sns.snsTag!!.id!!, type = updateTagType, values = updateTagValues),
                )

                // when
                snsRepository.update(sns = updateSns)
                entityManager.flush()
                entityManager.clear()

                // then
                val findSns: Sns = snsReadRepository.findWithAssociatedEntitiesById(snsId = sns.id!!)
                val findSnsImages: List<SnsImage> = findSns.snsImages
                val findSnsTag: SnsTag = findSns.snsTag!!

                findSns.shouldBe(updateSns)
                findSnsImages.shouldHaveSize(updateSns.snsImages.size)
                findSnsTag.shouldBe(updateSns.snsTag!!)
            }
        }

        this.describe("findWithAssociatedEntitiesById 메서드는") {
            it("삭제된 Sns 엔티티는 조회할 수 없다") {
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
                snsRepository.delete(id = sns.id!!)

                // when
                val exception = shouldThrow<NoSuchElementException> {
                    snsReadRepository.findWithAssociatedEntitiesById(snsId = sns.id!!)
                }

                // then
                exception.localizedMessage.shouldBe(NOT_FOUND_SNS)
            }
        }

        this.describe("delete 메서드는") {
            it("등록된 Sns 엔티티를 삭제 처리한다") {
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
                snsRepository.delete(id = sns.id!!)

                // then
                shouldThrow<NoSuchElementException> { snsReadRepository.findById(snsId = sns.id!!) }
                    .localizedMessage.shouldBe(NOT_FOUND_SNS)
            }
        }
    }
}
