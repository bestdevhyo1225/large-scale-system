package com.hyoseok.repository.post

import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.repository.RepositoryImplTests
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

internal class PostRepositoryTests : RepositoryImplTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var postReadRepository: PostReadRepository

    init {
        this.describe("save 메서드는") {
            it("Post, PostImage 엔티티를 저장한다") {
                // given
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

        this.describe("findById 메서드는") {
            it("Post 엔티티만 가져온다") {
                // given
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

                postRepository.save(post = post)
                entityManager.flush()
                entityManager.clear()

                // when
                val findPost: Post = postReadRepository.findById(id = post.id!!)

                // then
                findPost.id.shouldNotBeNull()
                findPost.id.shouldBe(post.id)
                findPost.memberId.shouldBe(post.memberId)
                findPost.title.shouldBe(post.title)
                findPost.contents.shouldBe(post.contents)
                findPost.writer.shouldBe(post.writer)
                findPost.images.shouldBeEmpty()
                findPost.createdAt.shouldBe(post.createdAt)
                findPost.updatedAt.shouldBe(post.updatedAt)
                findPost.deletedAt.shouldBe(post.deletedAt)
            }
        }

        this.describe("findByIdWithImages 메서드는") {
            it("Post, PostImage 엔티티를 가져온다") {
                // given
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

                postRepository.save(post = post)
                entityManager.flush()
                entityManager.clear()

                // when
                val findPost: Post = postReadRepository.findByIdWithImages(id = post.id!!)

                // then
                findPost.id.shouldNotBeNull()
                findPost.id.shouldBe(post.id)
                findPost.memberId.shouldBe(post.memberId)
                findPost.title.shouldBe(post.title)
                findPost.contents.shouldBe(post.contents)
                findPost.writer.shouldBe(post.writer)
                findPost.images.shouldNotBeEmpty()
                findPost.images.shouldHaveSize(post.images.size)
                findPost.createdAt.shouldBe(post.createdAt)
                findPost.updatedAt.shouldBe(post.updatedAt)
                findPost.deletedAt.shouldBe(post.deletedAt)
            }
        }

        this.describe("findRecentlyRegisteredAllByIds 메서드는") {
            it("Post, PostImage 엔티티 리스트를 가져온다") {
                // given
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

                postRepository.save(post = post)
                entityManager.flush()
                entityManager.clear()

                // when
                val posts: List<Post> = postReadRepository.findRecentlyRegisteredAllByIds(ids = listOf(post.id!!))

                posts.shouldNotBeEmpty()
                posts.size.shouldBe(1)
                posts.forEach {
                    it.id.shouldNotBeNull()
                    it.id.shouldBe(post.id)
                    it.memberId.shouldBe(post.memberId)
                    it.title.shouldBe(post.title)
                    it.contents.shouldBe(post.contents)
                    it.writer.shouldBe(post.writer)
                    it.images.shouldNotBeEmpty()
                    it.images.shouldHaveSize(post.images.size)
                    it.createdAt.shouldBe(post.createdAt)
                    it.updatedAt.shouldBe(post.updatedAt)
                    it.deletedAt.shouldBe(post.deletedAt)
                }
            }
        }

        this.describe("findRecentlyRegisteredAllByMemberIdAndPage 메서드는") {
            it("memberId 회원의 게시물 리스트를 조회한다") {
                // given
                val memberId = 1L
                val size = 10L
                (1L..size).forEach { _ ->
                    postRepository.save(
                        post = Post(
                            memberId = memberId,
                            title = "게시물 제목",
                            contents = "내용",
                            writer = "작성자",
                            images = listOf(
                                PostImage(url = "https://main/images.com", sortOrder = 0),
                                PostImage(url = "https://list/images.com", sortOrder = 1),
                            ),
                        ),
                    )
                }
                (1L..5L).forEach { _ ->
                    postRepository.save(
                        post = Post(
                            memberId = 2L,
                            title = "게시물 제목",
                            contents = "내용",
                            writer = "작성자",
                            images = listOf(
                                PostImage(url = "https://main/images.com", sortOrder = 0),
                                PostImage(url = "https://list/images.com", sortOrder = 1),
                            ),
                        ),
                    )
                }

                // when
                val limit = 5L
                val offset = 0L
                val (total: Long, posts: List<Post>) = postReadRepository.findRecentlyRegisteredAllByMemberIdAndPage(
                    memberId = memberId,
                    limit = limit,
                    offset = offset,
                )

                // then
                total.shouldNotBeZero()
                total.shouldBe(size)
                posts.shouldNotBeEmpty()
                posts.shouldHaveSize(limit.toInt())
            }
        }
    }
}
