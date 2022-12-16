package com.hyoseok.usecase

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.wish.service.WishReadService
import com.hyoseok.wish.service.WishRedisReadService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class FindPostUsecaseTests : BehaviorSpec(
    {
        val mockPostRedisReadService: PostRedisReadService = mockk()
        val mockPostRedisService: PostRedisService = mockk()
        val mockPostReadService: PostReadService = mockk()
        val mockWishRedisReadService: WishRedisReadService = mockk()
        val mockWishReadService: WishReadService = mockk()
        val findPostUsecase = FindPostUsecase(
            postRedisReadService = mockPostRedisReadService,
            postRedisService = mockPostRedisService,
            postReadService = mockPostReadService,
            wishRedisReadService = mockWishRedisReadService,
            wishReadService = mockWishReadService,
        )
        val postId = 1L
        val memberId = 1L
        val title = "게시물 제목"
        val contents = "게시물 내용"
        val writer = "작성자"
        val viewCount = 1234L
        val createdAt: LocalDateTime = LocalDateTime.now().withNano(0)
        val updatedAt: LocalDateTime = LocalDateTime.now().withNano(0)
        val imageId = 1L
        val imageUrl = "https://test.image/1"
        val imageSortOrder = 1

        given("게시물과 좋아요 캐시가 둘 다 존재하는 경우") {
            val wishCountCache = 1L
            val postCache = PostCacheDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = listOf(
                    PostImageCacheDto(id = imageId, url = imageUrl, sortOrder = imageSortOrder),
                ),
            )

            every { mockPostRedisReadService.findPostCache(id = postId) } returns postCache
            every { mockWishRedisReadService.findWishCountCache(postId = postId) } returns wishCountCache

            `when`("캐시 결과를 반환하며") {
                findPostUsecase.execute(postId = postId)

                then("이와 관련된 메서드들은 최소 1번씩은 호출된다") {
                    verify { mockPostRedisReadService.findPostCache(id = postId) }
                    verify { mockWishRedisReadService.findWishCountCache(postId = postId) }
                }
            }
        }

        given("게시물과 좋아요 캐시가 둘 다 존재하지 않는 경우") {
            val postCache: PostCacheDto? = null
            val wishCountCache: Long? = null
            val wishCount: Long = 1
            val postDto = PostDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = listOf(
                    PostImageDto(id = imageId, url = imageUrl, sortOrder = imageSortOrder),
                ),
            )
            val createPostCacheDto: PostCacheDto = with(receiver = postDto) {
                PostCacheDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }

            every { mockPostRedisReadService.findPostCache(id = postId) } returns postCache
            every { mockWishRedisReadService.findWishCountCache(postId = postId) } returns wishCountCache
            every { mockWishReadService.findWishCount(postId = postId) } returns wishCount
            every { mockPostReadService.findPost(id = postId) } returns postDto
            justRun { mockPostRedisService.createOrUpdate(dto = createPostCacheDto) }

            `when`("DB에서 조회한 게시물과 좋아요를 함께 반환하며") {
                findPostUsecase.execute(postId = postId)

                then("이와 관련된 메서드들은 최소 1번씩은 호출된다") {
                    verify { mockPostRedisReadService.findPostCache(id = postId) }
                    verify { mockWishRedisReadService.findWishCountCache(postId = postId) }
                    verify { mockWishReadService.findWishCount(postId = postId) }
                    verify { mockPostReadService.findPost(id = postId) }
                }
            }
        }

        given("게시물 캐시가 존재하지 않고, 좋아요 캐시만 존재하는 경우") {
            val postCache = PostCacheDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = listOf(
                    PostImageCacheDto(id = imageId, url = imageUrl, sortOrder = imageSortOrder),
                ),
            )
            val wishCountCache: Long? = null
            val wishCount: Long = 1
            val postDto = PostDto(
                id = postId,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = listOf(
                    PostImageDto(id = imageId, url = imageUrl, sortOrder = imageSortOrder),
                ),
            )
            val createPostCacheDto: PostCacheDto = with(receiver = postDto) {
                PostCacheDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }

            every { mockPostRedisReadService.findPostCache(id = postId) } returns postCache
            every { mockWishRedisReadService.findWishCountCache(postId = postId) } returns wishCountCache
            every { mockWishReadService.findWishCount(postId = postId) } returns wishCount
            every { mockPostReadService.findPost(id = postId) } returns postDto
            justRun { mockPostRedisService.createOrUpdate(dto = createPostCacheDto) }

            `when`("DB에서 조회한 게시물 결과를 좋아요 캐시와 함께 반환하며") {
                findPostUsecase.execute(postId = postId)

                then("이와 관련된 메서드들은 최소 1번씩은 호출된다") {
                    verify { mockPostRedisReadService.findPostCache(id = postId) }
                    verify { mockWishRedisReadService.findWishCountCache(postId = postId) }
                    verify { mockWishReadService.findWishCount(postId = postId) }
                    verify { mockPostReadService.findPost(id = postId) }
                }
            }
        }
    },
)
