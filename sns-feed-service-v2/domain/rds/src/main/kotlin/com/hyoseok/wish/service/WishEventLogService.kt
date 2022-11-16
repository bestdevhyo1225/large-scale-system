package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishEventLogDto
import com.hyoseok.wish.entity.WishEventLog
import com.hyoseok.wish.repository.WishEventLogReadRepositoryImpl.ErrorMessage.NOT_FOUND_WISH_EVENT_LOG
import com.hyoseok.wish.repository.WishEventLogRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WishEventLogService(
    private val wishEventLogRepository: WishEventLogRepository,
) {

    fun create(postId: Long, memberId: Long): WishEventLogDto {
        val savedWishEventLog: WishEventLog =
            wishEventLogRepository.save(WishEventLog(postId = postId, memberId = memberId))
        return with(receiver = savedWishEventLog) {
            WishEventLogDto(
                id = id!!,
                postId = postId,
                memberId = memberId,
                isProcessed = isProcessed,
                publishedAt = publishedAt,
                processedAt = processedAt,
            )
        }
    }

    fun completeWishEventProcessing(id: Long) {
        wishEventLogRepository.findByIdOrNull(id = id)
            ?.completeWishEventProcessing() ?: throw NoSuchElementException(NOT_FOUND_WISH_EVENT_LOG)
    }
}
