package com.hyoseok.usecase

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.dto.WishEventDto
import com.hyoseok.wish.dto.WishEventLogDto
import com.hyoseok.wish.producer.WishKafkaProducer
import com.hyoseok.wish.service.WishEventLogService
import com.hyoseok.wish.service.WishRedisService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class CreateWishUsecaseTests : BehaviorSpec(
    {
        val mockWishEventLogService: WishEventLogService = mockk()
        val mockWishRedisService: WishRedisService = mockk()
        val mockWishKafkaProducer: WishKafkaProducer = mockk()
        val createWishUsecase = CreateWishUsecase(
            wishEventLogService = mockWishEventLogService,
            wishRedisService = mockWishRedisService,
            wishKafkaProducer = mockWishKafkaProducer,
        )

        given("좋아요를 할 때") {
            `when`("좋아요 캐시를 저장한 다음, 좋아요 메시지를 Kafka에 전송하고") {
                val wishCacheDto = WishCacheDto(postId = 1L, memberId = 1L)
                val wishEventLogDto = WishEventLogDto(
                    id = 1L,
                    postId = wishCacheDto.postId,
                    memberId = wishCacheDto.memberId,
                    isProcessed = false,
                    publishedAt = LocalDateTime.now().withNano(0),
                    processedAt = null,
                )
                val wishEventDto = WishEventDto(
                    wishEventLogId = wishEventLogDto.id,
                    postId = wishCacheDto.postId,
                    memberId = wishCacheDto.memberId,
                )

                justRun { mockWishRedisService.create(dto = wishCacheDto) }
                every {
                    mockWishEventLogService.create(
                        postId = wishCacheDto.postId,
                        memberId = wishCacheDto.memberId,
                    )
                } returns wishEventLogDto
                justRun { mockWishKafkaProducer.sendAsync(event = wishEventDto) }

                createWishUsecase.execute(postId = wishCacheDto.postId, memberId = wishCacheDto.memberId)

                then("이와 관련된 메서드들은 최소 1번씩 호출된다") {
                    verify {
                        mockWishEventLogService.create(
                            postId = wishCacheDto.postId,
                            memberId = wishCacheDto.memberId,
                        )
                    }
                }
            }
        }
    },
)
