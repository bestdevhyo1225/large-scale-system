package com.hyoseok.usecase

import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.service.WishRedisService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class CreateWishUsecaseTests : BehaviorSpec(
    {
        val mockMemberReadService: MemberReadService = mockk()
        val mockPostReadService: PostReadService = mockk()
        val mockWishRedisService: WishRedisService = mockk()
        val createWishUsecase = CreateWishUsecase(
            memberReadService = mockMemberReadService,
            postReadService = mockPostReadService,
            wishRedisService = mockWishRedisService,
        )

        given("좋아요를 처리하기 위해 아래와 같은 상황이 주어지면") {
            val memberDto = MemberDto(
                id = 1L,
                name = "name",
                influencer = false,
                createdAt = LocalDateTime.now().withNano(0),
                lastLoginDatetime = LocalDateTime.now().withNano(0),
            )
            val postDto = PostDto(
                id = 1L,
                memberId = 2L,
                title = "title",
                contents = "contents",
                writer = "writer",
                wishCount = 11234L,
                createdAt = LocalDateTime.now().withNano(0),
                updatedAt = LocalDateTime.now().withNano(0),
                images = listOf(
                    PostImageDto(id = 1L, url = "url", sortOrder = 1),
                ),
            )
            val wishCacheDto = WishCacheDto(postId = 1L, memberId = 1L)

            every { mockMemberReadService.findMember(id = memberDto.id) } returns memberDto
            every { mockPostReadService.findPost(id = postDto.id) } returns postDto
            justRun { mockWishRedisService.create(dto = wishCacheDto) }

            `when`("좋아요 캐시를 저장하는데") {
                createWishUsecase.execute(postId = wishCacheDto.postId, memberId = wishCacheDto.memberId)

                then("이와 관련된 메서드들은 최소 1번씩 호출된다") {
                    verify { mockMemberReadService.findMember(id = memberDto.id) }
                    verify { mockPostReadService.findPost(id = postDto.id) }
                }
            }
        }
    },
)
