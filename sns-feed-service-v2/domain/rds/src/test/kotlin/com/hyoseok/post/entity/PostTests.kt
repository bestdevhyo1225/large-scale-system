package com.hyoseok.post.entity

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
    },
)
