package com.hyoseok.service.follow

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.follow.repository.FollowRepository
import com.hyoseok.service.dto.FollowCreateResultDto
import com.hyoseok.service.dto.MemberFindResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(transactionManager = "jpaTransactionManager")
class FollowService(
    private val followRepository: FollowRepository,
    private val followReadRepository: FollowReadRepository,
) {

    fun following(
        followerMemberDto: MemberFindResultDto,
        followeeMemberDto: MemberFindResultDto,
    ): FollowCreateResultDto {
        val followeeCount: Long = followReadRepository.countByFollowerId(followerId = followerMemberDto.id)
        val savedFollow: Follow = Follow.create(
            followerId = followerMemberDto.id,
            followeeId = followeeMemberDto.id,
            followeeCount = followeeCount,
        ).also { followRepository.save(follow = it) }
        return FollowCreateResultDto(follow = savedFollow)
    }
}
