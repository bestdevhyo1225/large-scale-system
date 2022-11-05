package com.hyoseok.post.entity

import com.hyoseok.post.entity.Post.ErrorMessage.INVALID_CURRENT_VIEW_COUNT_IS_ZERO
import com.hyoseok.post.entity.Post.ErrorMessage.INVALID_VIEW_COUNT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

internal class PostTests : DescribeSpec(
    {
        describe("invoke 메서드는") {
            it("Post, PostImage 엔티티를 저장한다") {
                // given
                val memberId = 1L
                val title = "title"
                val contents = "contents"
                val writer = "writer"

                // when
                val postImages: List<PostImage> = listOf(
                    PostImage(url = "test1", sortOrder = 1),
                    PostImage(url = "test2", sortOrder = 2),
                )
                val post = Post(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    postImages = postImages,
                )

                // then
                post.postImages.shouldHaveSize(postImages.size)
                post.postImages.forEach {
                    it.post.shouldNotBeNull()
                    it.post.shouldBe(post)
                }
            }
        }

        describe("increaseViewCount 메서드는") {
            context("입력되는 viewCount 값이 0 이하인 경우") {
                it("예외를 던진다") {
                    // given
                    val memberId = 1L
                    val title = "title"
                    val contents = "contents"
                    val writer = "writer"
                    val postImages: List<PostImage> = listOf(
                        PostImage(url = "test1", sortOrder = 1),
                        PostImage(url = "test2", sortOrder = 2),
                    )
                    val post = Post(
                        memberId = memberId,
                        title = title,
                        contents = contents,
                        writer = writer,
                        postImages = postImages,
                    )

                    // when
                    val exception1: IllegalArgumentException = shouldThrow { post.increaseViewCount(viewCount = 0) }
                    val exception2: IllegalArgumentException = shouldThrow { post.increaseViewCount(viewCount = -1) }

                    // then
                    exception1.localizedMessage.shouldBe(INVALID_VIEW_COUNT)
                    exception2.localizedMessage.shouldBe(INVALID_VIEW_COUNT)
                }
            }
        }

        describe("decreaseViewCount 메서드는") {
            context("입력되는 viewCount 값이 0 보다 작은 경우") {
                it("예외를 던진다") {
                    // given
                    val memberId = 1L
                    val title = "title"
                    val contents = "contents"
                    val writer = "writer"
                    val postImages: List<PostImage> = listOf(
                        PostImage(url = "test1", sortOrder = 1),
                        PostImage(url = "test2", sortOrder = 2),
                    )
                    val post = Post(
                        memberId = memberId,
                        title = title,
                        contents = contents,
                        writer = writer,
                        postImages = postImages,
                    )

                    // when
                    val exception1: IllegalArgumentException = shouldThrow { post.decreaseViewCount(viewCount = 0) }
                    val exception2: IllegalArgumentException = shouldThrow { post.decreaseViewCount(viewCount = -1) }

                    // then
                    exception1.localizedMessage.shouldBe(INVALID_VIEW_COUNT)
                    exception2.localizedMessage.shouldBe(INVALID_VIEW_COUNT)
                }
            }

            context("현재 viewCount 값이 0인 경우") {
                it("예외를 던진다") {
                    // given
                    val memberId = 1L
                    val title = "title"
                    val contents = "contents"
                    val writer = "writer"
                    val postImages: List<PostImage> = listOf(
                        PostImage(url = "test1", sortOrder = 1),
                        PostImage(url = "test2", sortOrder = 2),
                    )
                    val post = Post(
                        memberId = memberId,
                        title = title,
                        contents = contents,
                        writer = writer,
                        postImages = postImages,
                    )

                    // when
                    val exception: IllegalArgumentException = shouldThrow { post.decreaseViewCount(viewCount = 1) }

                    // then
                    exception.localizedMessage.shouldBe(INVALID_CURRENT_VIEW_COUNT_IS_ZERO)
                }
            }
        }
    },
)
