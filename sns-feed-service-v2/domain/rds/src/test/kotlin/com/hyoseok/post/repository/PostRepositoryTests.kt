package com.hyoseok.post.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
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
        PostRepository::class,
        PostReadRepository::class,
        PostReadRepositoryImpl::class,
    ],
)
internal class PostRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var postReadRepository: PostReadRepository

    init {
        this.afterSpec {
            withContext(Dispatchers.IO) {
                postRepository.deleteAll()
            }
        }

        this.describe("save 메서드는") {
            it("Post, PostImage 엔티티를 저장한다") {
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
                withContext(Dispatchers.IO) {
                    postRepository.save(post)
                }

                // then
                val findPost: Post = postReadRepository.findByIdWithPostImage(id = post.id!!)

                findPost.shouldBe(post)
                findPost.postImages.shouldHaveSize(postImages.size)
            }
        }
    }
}
