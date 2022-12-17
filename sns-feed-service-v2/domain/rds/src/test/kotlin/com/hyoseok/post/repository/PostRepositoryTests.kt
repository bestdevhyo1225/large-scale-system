package com.hyoseok.post.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

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

        this.describe("findAllByInId 메서드는") {
            it("id 리스트에 해당되는 Post 엔티티를 반환한다") {
                // given
                val memberId = 1L
                val title = "title"
                val contents = "contents"
                val writer = "writer"
                val posts: List<Post> = listOf(
                    Post(
                        memberId = memberId,
                        title = title,
                        contents = contents,
                        writer = writer,
                        postImages = listOf(
                            PostImage(url = "test1", sortOrder = 1),
                            PostImage(url = "test2", sortOrder = 2),
                        ),
                    ),
                    Post(
                        memberId = memberId,
                        title = title,
                        contents = contents,
                        writer = writer,
                        postImages = listOf(
                            PostImage(url = "test1", sortOrder = 1),
                            PostImage(url = "test2", sortOrder = 2),
                        ),
                    ),
                )

                withContext(Dispatchers.IO) {
                    postRepository.saveAll(posts)
                }

                // when
                val findPosts: List<Post> = postReadRepository.findAllByInId(ids = posts.map { it.id!! })

                // then
                findPosts.shouldNotBeEmpty()
                findPosts.shouldHaveSize(posts.size)
            }
        }

        this.describe("findAllByMemberIdAndLimitAndOffset 메서드는") {
            it("회원번호, limit, offset을 통해 Post 엔티티 리스트를 조회한다") {
                // given
                val limit = 5L
                val contents = "contents"
                val writer = "writer"
                val memberId = 1L
                val posts: List<Post> = (1L..5L).map { titleNo ->
                    Post(
                        memberId = memberId,
                        title = "title$titleNo",
                        contents = contents,
                        writer = writer,
                        postImages = listOf(
                            PostImage(url = "test1", sortOrder = 1),
                            PostImage(url = "test2", sortOrder = 2),
                        ),
                    )
                }

                withContext(Dispatchers.IO) {
                    postRepository.saveAll(posts)
                }

                // when
                val findPosts: List<Post> = postReadRepository.findAllByMemberIdAndLimitAndOffset(
                    memberId = memberId,
                    limit = limit,
                    offset = 0,
                )

                // then
                findPosts.shouldNotBeEmpty()
                findPosts.shouldHaveSize(limit.toInt())
            }
        }

        this.describe("findAllByMemberIdsAndCreatedAtAndLimitAndOffset 메서드는") {
            it("회원번호 리스트, limit, offset을 통해 Post 엔티티 리스트를 조회한다") {
                // given
                val limit = 5L
                val contents = "contents"
                val writer = "writer"
                val memberIds: List<Long> = listOf(1L, 2L, 3L)
                val posts: List<Post> = memberIds.flatMap { memberId ->
                    (1L..3L).map { titleNo ->
                        Post(
                            memberId = memberId,
                            title = "title$titleNo",
                            contents = contents,
                            writer = writer,
                            postImages = listOf(
                                PostImage(url = "test1", sortOrder = 1),
                                PostImage(url = "test2", sortOrder = 2),
                            ),
                        )
                    }
                }

                withContext(Dispatchers.IO) {
                    postRepository.saveAll(posts)
                }

                // when
                val findPosts: List<Post> = postReadRepository.findAllByMemberIdsAndCreatedAtAndLimitAndOffset(
                    memberIds = memberIds,
                    fromCreatedAt = LocalDateTime.now().minusDays(1),
                    toCreatedAt = LocalDateTime.now().plusDays(1),
                    limit = limit,
                    offset = 0,
                )

                // then
                findPosts.shouldNotBeEmpty()
                findPosts.shouldHaveSize(limit.toInt())
                findPosts.map { it.memberId.shouldBeIn(memberIds) }
            }
        }
    }
}
