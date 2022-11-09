package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisTransactionRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class WishRedisServiceTests : DescribeSpec(
    {
        val mockWishRedisTransactionRepository: WishRedisTransactionRepository = mockk()
        val wishRedisService = WishRedisService(wishRedisTransactionRepository = mockWishRedisTransactionRepository)

        describe("create 메서드는") {
            it("좋아요 캐시를 저장한다") {
                // given
                val wishCacheDto = WishCacheDto(postId = 1L, memberId = 1L)
                val wishCache: WishCache = wishCacheDto.toEntity()

                every { mockWishRedisTransactionRepository.createWish(wishCache = wishCache) } returns true

                // when
                wishRedisService.create(dto = wishCacheDto)

                // then
                verify { mockWishRedisTransactionRepository.createWish(wishCache = wishCache) }
            }
        }
    },
)
