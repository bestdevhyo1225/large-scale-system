package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.mapper.PostCacheDtoMapper
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postRedisService: PostRedisService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(postId: Long): PostDto {
        val postCacheDto: PostCacheDto? = postRedisReadService.findPostCache(id = postId)

        if (postCacheDto != null) {
            return PostDtoMapper.of(postCacheDto = postCacheDto)
        }

        val postDto: PostDto = postReadService.findPost(id = postId)

        CoroutineScope(context = Dispatchers.IO).launch {
            postRedisService.createOrUpdate(dto = PostCacheDtoMapper.of(postDto = postDto))
        }

        return postDto
    }

    private fun fallbackExecute(exception: Exception): PostDto {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 해당 게시물을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}
