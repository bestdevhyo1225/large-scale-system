package com.hyoseok.service.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.service.dto.FollowCreateDto
import com.hyoseok.service.dto.FollowCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowService(
    private val followRepository: FollowRepository,
) {

    fun following(dto: FollowCreateDto): FollowCreateResultDto {
        val follow: Follow = dto.toEntity()
        followRepository.save(follow = follow)
        return FollowCreateResultDto(follow = follow)
    }
}
