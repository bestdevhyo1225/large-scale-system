package com.hyoseok.service.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.service.dto.FollowCreateDto
import com.hyoseok.service.dto.FollowCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(transactionManager = "jpaTransactionManager")
class FollowService(
    private val followRepository: FollowRepository,
    private val followReadRepository: FollowReadRepository,
    private val memberReadRepository: MemberReadRepository,
) {

    fun following(dto: FollowCreateDto): FollowCreateResultDto {
        val follow: Follow = dto.toEntity()

        if (existsMember(id = dto.followerId) && existsMember(id = dto.followeeId)) {
            Follow.checkFolloweeCount(value = followReadRepository.countByFollowerId(followerId = dto.followerId))
            followRepository.save(follow = follow)
        }

        return FollowCreateResultDto(follow = follow)
    }

    private fun existsMember(id: Long): Boolean = memberReadRepository.exists(id = id)
}
