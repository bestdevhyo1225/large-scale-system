package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.justRun
import io.mockk.mockk
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

internal class WishRedisServiceTests : DescribeSpec(
    {
        val mockWishRedisRepository: WishRedisRepository = mockk(relaxed = true)
        val wishRedisService = WishRedisService(wishRedisRepository = mockWishRedisRepository)

        describe("create 메서드는") {
            it("좋아요 캐시를 저장한다") {
                // given
                val wishCacheDto = WishCacheDto(postId = 1L, memberId = 1L)
                val wishCache: WishCache = wishCacheDto.toEntity()
                val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()

                justRun {
                    mockWishRedisRepository.zaddAndExpire(
                        key = wishCache.getWishPostKey(),
                        value = wishCache.memberId,
                        score = score,
                        expireTime = wishCache.expireTime,
                        timeUnit = SECONDS,
                    )
                }

                wishRedisService.create(dto = wishCacheDto)
            }
        }
    },
)
