package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POSTS_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.usecase.dto.FindPostWishUsecaseDto
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import com.hyoseok.wish.service.WishReadService
import com.hyoseok.wish.service.WishRedisReadService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
    private val wishRedisReadService: WishRedisReadService,
    private val wishReadService: WishReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POSTS_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<FindPostWishUsecaseDto> {
        val postPageByPosition: PageByPosition<PostDto> =
            postReadService.findPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        val postIds: List<Long> = postPageByPosition.items.map { it.id }
        val wishCounts: Map<Long, Long> = wishReadService.findWishCounts(postIds = postIds)
        val findPostWishUsecaseDtos: List<FindPostWishUsecaseDto> = postPageByPosition.items.map {
            FindPostWishUsecaseDto(postDto = it, wishCount = wishCounts[it.id] ?: 0L)
        }

        return PageByPosition(
            items = findPostWishUsecaseDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = findPostWishUsecaseDtos.size),
        )
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<FindPostWishUsecaseDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 게시물들을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}
