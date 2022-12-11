package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.usecase.dto.FindPostWishUsecaseDto
import com.hyoseok.wish.service.WishReadService
import com.hyoseok.wish.service.WishRedisReadService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postRedisService: PostRedisService,
    private val postReadService: PostReadService,
    private val wishRedisReadService: WishRedisReadService,
    private val wishReadService: WishReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(postId: Long): FindPostWishUsecaseDto = runBlocking {
        val deferredPostCache: Deferred<PostCacheDto?> = async(context = Dispatchers.IO) {
            getPostCache(postId = postId)
        }
        val deferredWishCount: Deferred<Long?> = async(context = Dispatchers.IO) {
            getWishCount(postId = postId)
        }

        val postCacheDto: PostCacheDto? = deferredPostCache.await()
        val wishCount: Long? = deferredWishCount.await()

        if (postCacheDto != null && wishCount != null) {
            return@runBlocking getFindPostWishUsecaseDto(postCacheDto = postCacheDto, wishCount = wishCount)
        }

        val postDto: PostDto = postReadService.findPost(id = postId)

        CoroutineScope(context = Dispatchers.IO).launch {
            postRedisService.createOrUpdate(dto = createPostCacheDto(postDto = postDto))
        }

        if (wishCount == null) {
            return@runBlocking FindPostWishUsecaseDto(
                postDto = postDto,
                wishCount = wishReadService.getCountByPostId(postId = postId),
            )
        }

        FindPostWishUsecaseDto(postDto = postDto, wishCount = wishCount)
    }

    private suspend fun getPostCache(postId: Long): PostCacheDto? = postRedisReadService.findPostCache(id = postId)

    private suspend fun getWishCount(postId: Long): Long? = wishRedisReadService.findWishCount(postId = postId)

    private fun getFindPostWishUsecaseDto(postCacheDto: PostCacheDto, wishCount: Long): FindPostWishUsecaseDto =
        FindPostWishUsecaseDto(postCacheDto = postCacheDto, wishCount = wishCount)

    private fun createPostCacheDto(postDto: PostDto): PostCacheDto =
        with(receiver = postDto) {
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

    private fun fallbackExecute(exception: Exception): FindPostWishUsecaseDto {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 해당 게시물을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}
