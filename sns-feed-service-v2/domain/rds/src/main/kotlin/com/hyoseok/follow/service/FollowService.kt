package com.hyoseok.follow.service

import com.hyoseok.follow.dto.FollowCreateDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.FollowCount
import com.hyoseok.follow.repository.FollowCountRepository
import com.hyoseok.follow.repository.FollowRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowService(
    private val followRepository: FollowRepository,
    private val followCountRepository: FollowCountRepository,
) {

    fun create(dto: FollowCreateDto): FollowDto {
        val savedFollow: Follow = Follow(followerId = dto.followerId, followeeId = dto.followeeId)
            .run { followRepository.save(this) }

        createOrIncreaseFollowerCount(followeeId = dto.followeeId)
        createOrIncreaseFolloweeCount(followerId = dto.followerId)

        return with(receiver = savedFollow) {
            FollowDto(id = id!!, followerId = followerId, followeeId = followeeId, createdAt = createdAt)
        }
    }

    private fun createOrIncreaseFollowerCount(followeeId: Long) {
        val followeeFollowCount: FollowCount? = followCountRepository.findByIdOrNull(followeeId)

        if (followeeFollowCount == null) {
            followCountRepository.save(FollowCount(memberId = followeeId, totalFollower = 1, totalFollowee = 0))
        } else {
            followeeFollowCount.increaseTotalFollowerOne()
        }
    }

    private fun createOrIncreaseFolloweeCount(followerId: Long) {
        val followerFollowCount: FollowCount? = followCountRepository.findByIdOrNull(followerId)

        if (followerFollowCount == null) {
            followCountRepository.save(FollowCount(memberId = followerId, totalFollower = 0, totalFollowee = 1))
        } else {
            followerFollowCount.increaseTotalFolloweeOne()
        }
    }
}
