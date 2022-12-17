package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.CREATE_WISH_USECASE
import com.hyoseok.exception.ApiRateLimitException
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.service.WishRedisService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CreateWishUsecase(
    private val memberReadService: MemberReadService,
    private val postReadService: PostReadService,
    private val wishRedisService: WishRedisService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = CREATE_WISH_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(postId: Long, memberId: Long) {
        val memberDto: MemberDto = memberReadService.findMember(id = memberId)
        val postDto: PostDto = postReadService.findPost(id = postId)
        wishRedisService.create(dto = WishCacheDto(postId = postDto.id, memberId = memberDto.id))
    }

    private fun fallbackExecute(exception: Exception) {
        logger.error { exception.localizedMessage }
        throw ApiRateLimitException(message = "일시적으로 좋아요를 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}
