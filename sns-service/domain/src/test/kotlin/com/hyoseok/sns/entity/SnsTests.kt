package com.hyoseok.sns.entity

import com.hyoseok.exception.Message.EXCEEDS_SNS_IMAGES_SIZE
import com.hyoseok.exception.Message.EXCEEDS_SNS_TAGS_SIZE
import com.hyoseok.exception.Message.NOT_EXISTS_TAG_TYPE
import com.hyoseok.sns.entity.enums.SnsTagType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

internal class SnsTests : DescribeSpec(
    {
        describe("createSnsImages 메서드는") {
            it("SnsImage 엔티티를 생성한다.") {
                // given
                val snsImages = listOf(Pair("image0", 0), Pair("image1", 1), Pair("image2", 2))

                // when
                val createSnsImages = SnsImage.createSnsImages(snsImages = snsImages)

                // then
                createSnsImages.forEachIndexed { index, snsImage ->
                    snsImage.url.shouldBe(snsImages[index].first)
                    snsImage.sortOrder.shouldBe(snsImages[index].second)
                }
            }

            context("이미지 리스트의 수가 10개를 넘으면") {
                it("IllegalArgumentException 예외를 던진다.") {
                    // given
                    val snsImages = (0..15).map { Pair("image$it", it) }

                    // when
                    val exception =
                        shouldThrow<IllegalArgumentException> { SnsImage.createSnsImages(snsImages = snsImages) }

                    // then
                    exception.localizedMessage.shouldBe(EXCEEDS_SNS_IMAGES_SIZE)
                }
            }
        }

        describe("SnsTag 엔티티의 invoke 메서드는") {
            it("SnsTag 엔티티를 생성한다.") {
                // given
                val type = "style"
                val values = listOf("캐주얼", "출근")

                // when
                val snsTag = SnsTag(type = type, values = values)

                // then
                snsTag.type.shouldBe(SnsTagType(value = type))
                snsTag.values.containsAll(values)
            }

            context("type 값이 존재하지 않는 경우") {
                it("IllegalArgumentException 예외를 던진다.") {
                    // given
                    val type = "style-tpo?"
                    val values = listOf("캐주얼", "출근")

                    // when
                    val exception =
                        shouldThrow<IllegalArgumentException> { SnsTag(type = type, values = values) }

                    // then
                    exception.localizedMessage.shouldBe(NOT_EXISTS_TAG_TYPE)
                }
            }

            context("values 리스트가 허용 범위를 초과한 경우") {
                it("IllegalArgumentException 예외를 던진다.") {
                    val type = "style"
                    val values = listOf("캐주얼", "출근", "여행/여가", "나들이")

                    // when
                    val exception =
                        shouldThrow<IllegalArgumentException> { SnsTag(type = type, values = values) }

                    // then
                    exception.localizedMessage.shouldBe(EXCEEDS_SNS_TAGS_SIZE)
                }
            }
        }

        context("Sns 엔티티의 invoke 메서드는") {
            it("Sns, SnsImage, SnsTag 엔티티를 생성한다.") {
                // given
                val title = "title"
                val contents = "contents"
                val writer = "writer"
                val snsImages = listOf(Pair("image0", 0), Pair("image1", 1), Pair("image2", 2))
                val tagType = "tpo"
                val tagValues = listOf("파티", "나들이")

                // when
                val sns = Sns(
                    title = title,
                    contents = contents,
                    writer = writer,
                    snsImages = snsImages,
                    tagType = tagType,
                    tagValues = tagValues,
                )

                // then
                sns.title.shouldBe(title)
                sns.contents.shouldBe(contents)
                sns.writer.shouldBe(writer)
                sns.isDisplay.shouldBeTrue()
                sns.createdAt.shouldNotBeNull()
                sns.updatedAt.shouldNotBeNull()
                sns.deletedAt.shouldBeNull()
                sns.snsImages.forEachIndexed { index, snsImage ->
                    snsImage.url.shouldBe(snsImages[index].first)
                    snsImage.sortOrder.shouldBe(snsImages[index].second)
                }
                sns.snsTag.type.shouldBe(SnsTagType(value = tagType))
                sns.snsTag.values.containsAll(tagValues)
            }
        }
    },
)
