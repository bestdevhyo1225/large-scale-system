package com.hyoseok.follow.service

import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.FollowCount
import com.hyoseok.follow.entity.FollowCount.Companion.INFLUENCER_MIN_LIMIT_COUNT
import com.hyoseok.follow.repository.FollowCountReadRepository
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.follow.service.FollowReadService.ErrorMessage.FAILED_UPDATE_INFLUENCER
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FollowReadService(
    private val followReadRepository: FollowReadRepository,
    private val followCountReadRepository: FollowCountReadRepository,
) {

    object ErrorMessage {
        const val FAILED_UPDATE_INFLUENCER = "인플루언서로 변경할 수 있는 조건이 아닙니다."
    }

    fun checkInfluencer(followeeId: Long) {
        if (followReadRepository.countByFolloweeId(followeeId = followeeId) < INFLUENCER_MIN_LIMIT_COUNT) {
            throw IllegalArgumentException(FAILED_UPDATE_INFLUENCER)
        }
    }

    fun findFollowerIds(followeeId: Long, influencer: Boolean, limit: Long, offset: Long): Pair<Long, List<Long>> {
        // 인플루언서의 경우, 이벤트를 발행하지 않겠다.
        // 게시물 하나를 등록할 때마다 이벤트 발행하는 프로세스의 부하가 심하다.
        if (influencer) {
            return Pair(first = 0L, second = listOf())
        }

        val (total: Long, followers: List<Follow>) = followReadRepository.findAllByFolloweeIdAndLimitAndOffset(
            followeeId = followeeId,
            limit = limit,
            offset = offset,
        )

        return Pair(first = total, second = followers.map { it.followerId })
    }

    fun findFollowers(followeeId: Long, lastId: Long, limit: Long): List<FollowDto> {
        val followCount: FollowCount =
            followCountReadRepository.findByMemberId(memberId = followeeId) ?: return listOf()

        if (followCount.totalFollower >= INFLUENCER_MIN_LIMIT_COUNT) {
            return listOf()
        }

        return followReadRepository.findAllByFolloweeIdAndLastIdAndLimit(
            followeeId = followeeId,
            lastId = lastId,
            limit = limit,
        ).map { FollowDto(follow = it) }
    }

    fun findInfluencerFolloweeIds(followerId: Long, findFolloweeMaxLimit: Long): List<Long> =
        followReadRepository.findAllByFollowerIdAndLimitOrderByIdDesc(
            followerId = followerId,
            checkTotalFollower = INFLUENCER_MIN_LIMIT_COUNT,
            limit = findFolloweeMaxLimit,
        ).map { it.followeeId }
}
