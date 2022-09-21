package com.hyoseok.service.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.service.dto.FollowCreateDto
import com.hyoseok.service.dto.FollowCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowService(
    private val followRepository: FollowRepository,
    private val memberReadRepository: MemberReadRepository,
) {

    fun following(dto: FollowCreateDto): FollowCreateResultDto {
        val follow: Follow = dto.toEntity()

        if (memberReadRepository.exists(id = dto.followeeId)) {
            followRepository.save(follow = follow)
        }

        return FollowCreateResultDto(follow = follow)
    }
}
