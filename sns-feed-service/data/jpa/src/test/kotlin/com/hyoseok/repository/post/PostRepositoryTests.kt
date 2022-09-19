package com.hyoseok.repository.post

import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class PostRepositoryTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var postRepository: PostRepository

    init {
        this.describe("save 메서드는") {
            it("Post, PostImage 엔티티를 저장한다") {
                val memberId = 1L
                val title = "게시물 제목"
                val contents = "내용"
                val writer = "작성자"
                val images: List<PostImage> = listOf(
                    PostImage(url = "https://main/images.com", sortOrder = 0),
                    PostImage(url = "https://list/images.com", sortOrder = 1),
                )
                val post = Post(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    images = images,
                )

                // when
                postRepository.save(post = post)

                // then
                post.id.shouldNotBeNull()
                post.images.forEach { it.id.shouldNotBeNull() }
            }
        }
    }
}
