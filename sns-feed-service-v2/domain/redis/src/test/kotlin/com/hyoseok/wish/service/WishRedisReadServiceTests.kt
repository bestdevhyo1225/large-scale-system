package com.hyoseok.wish.service

import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisPipelineRepository
import com.hyoseok.wish.repository.WishRedisRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class WishRedisReadServiceTests : DescribeSpec(
    {
        val mockWishRedisRepository: WishRedisRepository = mockk()
        val mockWishRedisPipelineRepository: WishRedisPipelineRepository = mockk()
        val wishRedisReadService = WishRedisReadService(
            wishRedisRepository = mockWishRedisRepository,
            wishRedisPipelineRepository = mockWishRedisPipelineRepository,
        )

        describe("findWishCount 메서드는") {
            it("게시글에 대한 좋아요 수를 반환한다") {
                // given
                val postId = 1L
                val key: String = WishCache.getWishPostKey(postId = postId)

                every { mockWishRedisRepository.scard(key = key) } returns 1L

                // when
                wishRedisReadService.findWishCountCache(postId = postId)

                // then
                verify { mockWishRedisRepository.scard(key = key) }
            }
        }
    },
)
