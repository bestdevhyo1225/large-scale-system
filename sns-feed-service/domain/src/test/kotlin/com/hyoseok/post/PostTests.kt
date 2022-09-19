package com.hyoseok.post

import com.hyoseok.post.entity.Post
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class PostTests : DescribeSpec(
    {
        describe("Post의 invoke 메서드를 통해") {
            it("Post, PostImage 엔티티를 생성한다") {
                // given
                val memberId = 1L
                val title = "게시물 제목"
                val contents = "내용"
                val writer = "작성자"
                val images: List<Pair<String, Int>> = listOf(
                    Pair(first = "https://main/images.com", second = 0),
                    Pair(first = "https://list/images.com", second = 1),
                )

                // when
                val post = Post(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    images = images,
                )

                // then
                post.memberId.shouldBe(memberId)
                post.title.shouldBe(title)
                post.contents.shouldBe(contents)
                post.writer.shouldBe(writer)
                post.images.forEachIndexed { index, postImage ->
                    postImage.url.shouldBe(images[index].first)
                    postImage.sortOrder.shouldBe(images[index].second)
                }
            }
        }
    },
)
