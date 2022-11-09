package com.hyoseok.wish.service

import com.hyoseok.wish.entity.Wish
import com.hyoseok.wish.repository.WishRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class WishServiceTests : DescribeSpec(
    {
        val mockWishRepository: WishRepository = mockk()
        val wishService = WishService(wishRepository = mockWishRepository)

        describe("create 메서드는") {
            it("좋아요를 저장한다") {
                val wish: Wish = mockk {
                    every { id } returns 1L
                    every { postId } returns 1L
                    every { memberId } returns 1L
                    every { createdAt } returns LocalDateTime.now()
                }

                every { mockWishRepository.save(any()) } returns wish

                withContext(Dispatchers.IO) {
                    wishService.create(postId = wish.postId, memberId = wish.memberId)
                }

                verify { mockWishRepository.save(any()) }
            }
        }
    },
)
