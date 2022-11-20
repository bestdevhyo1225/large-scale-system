package com.hyoseok.follow.service

import com.hyoseok.follow.dto.FollowCreateDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowService(
    private val followRepository: FollowRepository,
) {

    fun create(dto: FollowCreateDto): FollowDto =
        Follow(followerId = dto.followerId, followeeId = dto.followeeId)
            .run { followRepository.save(this) }
            .let {
                with(receiver = it) {
                    FollowDto(id = id!!, followerId = followerId, followeeId = followeeId, createdAt = createdAt)
                }
            }
}
